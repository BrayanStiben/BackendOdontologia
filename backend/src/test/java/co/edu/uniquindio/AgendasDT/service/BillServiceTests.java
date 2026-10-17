package co.edu.uniquindio.AgendasDT.service;

import co.edu.uniquindio.AgendasDT.domain.User;
import co.edu.uniquindio.AgendasDT.dto.BillCreateRequest;
import co.edu.uniquindio.AgendasDT.dto.BillItemRequest;
import co.edu.uniquindio.AgendasDT.repository.BillDetailRepository;
import co.edu.uniquindio.AgendasDT.repository.BillRepository;
import co.edu.uniquindio.AgendasDT.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BillServiceTests {
    @Mock BillRepository bills;
    @Mock BillDetailRepository billDetails;
    @Mock UserRepository users;

    Clock clock = Clock.fixed(Instant.parse("2026-09-09T08:30:00Z"), ZoneOffset.UTC);
    BillService service;

    @BeforeEach
    void setUp() {
        service = new BillService(bills, billDetails, users, clock);
    }

    @Test
    void createBillCalculatesSubtotalDiscountAndTotal() {
        User patient = new User();
        patient.setId(1094821402L);
        patient.setName("Maria");
        patient.setLastName("Garcia Perez");

        when(users.findById(1094821402L)).thenReturn(Optional.of(patient));

        List<BillItemRequest> items = List.of(
                new BillItemRequest("CUPS-890201", "Consulta de primera vez", 1, 60000.0, 0.0),
                new BillItemRequest("CUPS-232101", "Resina de fotocurado", 1, 140000.0, 10.0),
                new BillItemRequest("CUPS-997301", "Detartraje y profilaxis", 1, 80000.0, 0.0)
        );

        var req = new BillCreateRequest(1094821402L, "Tarjeta de Credito", "Cra 15 #22-10, Armenia",
                "Tratamiento en sesion unica.", items);

        var res = service.create(req);

        assertNotNull(res);
        assertTrue(res.billNumber().startsWith("FE-2026-"));
        assertEquals(280000.0, res.subtotal());
        assertEquals(14000.0, res.discount());
        assertEquals(0.0, res.tax());
        assertEquals(266000.0, res.totalCost());
        assertNotNull(res.cufe());
        assertEquals(3, res.items().size());
        verify(bills).saveAndFlush(any());
        verify(billDetails, times(3)).save(any());
    }
}

