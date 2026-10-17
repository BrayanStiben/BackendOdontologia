package co.edu.uniquindio.AgendasDT.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Table(name = "bill")
@Entity
public class Bill {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private UUID id;

    @Column(name = "bill_number", nullable = false, unique = true, length = 50)
    private String billNumber;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "patient_id", nullable = false)
    private User patient;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receptionist_id")
    private User receptionist;

    @Column(name = "issue_date", nullable = false)
    private Instant issueDate;

    @Column(name = "payment_method", length = 50)
    private String paymentMethod;

    @Column(name = "subtotal", nullable = false)
    private Double subtotal = 0.0;

    @Column(name = "discount", nullable = false)
    private Double discount = 0.0;

    @Column(name = "tax", nullable = false)
    private Double tax = 0.0;

    @Column(name = "total_cost", nullable = false)
    private Double totalCost = 0.0;

    @Column(name = "observations", length = 1000)
    private String observations;

    @Column(name = "cufe", length = 255)
    private String cufe;

    @OneToMany(mappedBy = "bill", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BillDetail> details = new ArrayList<>();

    public Bill() {
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public String getBillNumber() { return billNumber; }
    public void setBillNumber(String billNumber) { this.billNumber = billNumber; }

    public User getPatient() { return patient; }
    public void setPatient(User patient) { this.patient = patient; }

    public User getReceptionist() { return receptionist; }
    public void setReceptionist(User receptionist) { this.receptionist = receptionist; }

    public Instant getIssueDate() { return issueDate; }
    public void setIssueDate(Instant issueDate) { this.issueDate = issueDate; }

    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }

    public Double getSubtotal() { return subtotal; }
    public void setSubtotal(Double subtotal) { this.subtotal = subtotal; }

    public Double getDiscount() { return discount; }
    public void setDiscount(Double discount) { this.discount = discount; }

    public Double getTax() { return tax; }
    public void setTax(Double tax) { this.tax = tax; }

    public Double getTotalCost() { return totalCost; }
    public void setTotalCost(Double totalCost) { this.totalCost = totalCost; }

    public String getObservations() { return observations; }
    public void setObservations(String observations) { this.observations = observations; }

    public String getCufe() { return cufe; }
    public void setCufe(String cufe) { this.cufe = cufe; }

    public List<BillDetail> getDetails() { return details; }
    public void setDetails(List<BillDetail> details) { this.details = details; }
}
