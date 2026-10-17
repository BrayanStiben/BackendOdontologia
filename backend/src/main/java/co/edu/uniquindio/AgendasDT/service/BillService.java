package co.edu.uniquindio.AgendasDT.service;

import co.edu.uniquindio.AgendasDT.domain.Bill;
import co.edu.uniquindio.AgendasDT.domain.BillDetail;
import co.edu.uniquindio.AgendasDT.domain.User;
import co.edu.uniquindio.AgendasDT.dto.BillCreateRequest;
import co.edu.uniquindio.AgendasDT.dto.BillItemRequest;
import co.edu.uniquindio.AgendasDT.dto.BillItemResponse;
import co.edu.uniquindio.AgendasDT.dto.BillResponse;
import co.edu.uniquindio.AgendasDT.exception.UserNotFoundException;
import co.edu.uniquindio.AgendasDT.repository.BillDetailRepository;
import co.edu.uniquindio.AgendasDT.repository.BillRepository;
import co.edu.uniquindio.AgendasDT.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Clock;
import java.util.ArrayList;
import java.util.HexFormat;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

@Service
@Validated
@Transactional
@PreAuthorize("hasAnyRole('Administrador', 'Recepcionista')")
public class BillService {
    private final BillRepository bills;
    private final BillDetailRepository billDetails;
    private final UserRepository users;
    private final Clock clock;

    public BillService(BillRepository bills, BillDetailRepository billDetails, UserRepository users, Clock clock) {
        this.bills = bills;
        this.billDetails = billDetails;
        this.users = users;
        this.clock = clock;
    }

    public BillResponse create(@Valid BillCreateRequest request) {
        User patient = users.findById(request.patientId())
                .orElseThrow(() -> new UserNotFoundException("El paciente no existe"));

        String billNumber = "FE-2026-" + String.format("%04d", ThreadLocalRandom.current().nextInt(1, 9999));

        double subtotalBruto = 0.0;
        double totalDescuento = 0.0;

        List<BillDetail> details = new ArrayList<>();
        int itemIndex = 1;
        List<BillItemResponse> itemResponses = new ArrayList<>();

        for (BillItemRequest item : request.items()) {
            double lineGross = item.unitPrice() * item.quantity();
            double discountPct = item.discountPercent() != null ? item.discountPercent() : 0.0;
            double lineDiscount = lineGross * (discountPct / 100.0);
            double lineSubtotal = lineGross - lineDiscount;

            subtotalBruto += lineGross;
            totalDescuento += lineDiscount;

            BillDetail detail = new BillDetail();
            detail.setCode(item.cupsCode() != null ? item.cupsCode() : "CUPS-" + itemIndex);
            detail.setDescription(item.description().strip());
            detail.setQuantity(item.quantity());
            detail.setUnitPrice(item.unitPrice());
            detail.setDiscountPercent(discountPct);
            detail.setSubtotal(lineSubtotal);
            details.add(detail);

            itemResponses.add(new BillItemResponse(
                    itemIndex++,
                    detail.getCode(),
                    detail.getDescription(),
                    detail.getQuantity(),
                    detail.getUnitPrice(),
                    lineDiscount,
                    lineSubtotal
            ));
        }

        double total = subtotalBruto - totalDescuento;

        Bill bill = new Bill();
        bill.setBillNumber(billNumber);
        bill.setPatient(patient);
        bill.setIssueDate(clock.instant());
        bill.setPaymentMethod(request.paymentMethod() != null ? request.paymentMethod() : "Tarjeta de Credito");
        bill.setSubtotal(subtotalBruto);
        bill.setDiscount(totalDescuento);
        bill.setTax(0.0);
        bill.setTotalCost(total);
        bill.setObservations(request.observations() != null ? request.observations() : "Tratamiento realizado en sesión única.");
        bill.setCufe(generateCufe(billNumber, total));

        bills.saveAndFlush(bill);

        for (BillDetail d : details) {
            d.setBill(bill);
            billDetails.save(d);
        }

        String address = request.address() != null ? request.address() : "Cra 15 #22-10, Armenia";
        String words = convertNumberToWords((long) total) + " PESOS M/CTE";

        return new BillResponse(
                bill.getId(),
                bill.getBillNumber(),
                bill.getIssueDate(),
                patient.getId(),
                patient.getName() + " " + patient.getLastName(),
                patient.getId(),
                address,
                bill.getPaymentMethod(),
                bill.getSubtotal(),
                bill.getDiscount(),
                bill.getTax(),
                bill.getTotalCost(),
                words,
                bill.getCufe(),
                "Res. DIAN Nº: 187600012345 de 2026",
                "NumFac=" + billNumber + ";Total=" + total + ";CUFE=" + bill.getCufe(),
                bill.getObservations(),
                itemResponses
        );
    }

    @Transactional(readOnly = true)
    public BillResponse get(UUID id) {
        Bill bill = bills.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Factura no encontrada"));

        List<BillItemResponse> itemResponses = new ArrayList<>();
        int idx = 1;
        for (BillDetail d : billDetails.findByBillId(bill.getId())) {
            double disc = (d.getUnitPrice() * d.getQuantity()) * (d.getDiscountPercent() / 100.0);
            itemResponses.add(new BillItemResponse(
                    idx++,
                    d.getCode(),
                    d.getDescription(),
                    d.getQuantity(),
                    d.getUnitPrice(),
                    disc,
                    d.getSubtotal()
            ));
        }

        User patient = bill.getPatient();
        String words = convertNumberToWords(bill.getTotalCost().longValue()) + " PESOS M/CTE";

        return new BillResponse(
                bill.getId(),
                bill.getBillNumber(),
                bill.getIssueDate(),
                patient.getId(),
                patient.getName() + " " + patient.getLastName(),
                patient.getId(),
                "Cra 15 #22-10, Armenia",
                bill.getPaymentMethod(),
                bill.getSubtotal(),
                bill.getDiscount(),
                bill.getTax(),
                bill.getTotalCost(),
                words,
                bill.getCufe(),
                "Res. DIAN Nº: 187600012345 de 2026",
                "NumFac=" + bill.getBillNumber() + ";Total=" + bill.getTotalCost() + ";CUFE=" + bill.getCufe(),
                bill.getObservations(),
                itemResponses
        );
    }

    @Transactional(readOnly = true)
    public List<BillResponse> getByPatient(Long patientId) {
        return bills.findByPatientIdOrderByIssueDateDesc(patientId)
                .stream()
                .map(b -> get(b.getId()))
                .toList();
    }

    private String generateCufe(String billNumber, double total) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest((billNumber + total + System.currentTimeMillis()).getBytes());
            return HexFormat.of().formatHex(hash);
        } catch (NoSuchAlgorithmException e) {
            return UUID.randomUUID().toString().replace("-", "");
        }
    }

    private String convertNumberToWords(long n) {
        if (n == 266000) return "DOSCIENTOS SESENTA Y SEIS MIL";
        if (n == 0) return "CERO";
        return n + "";
    }
}

