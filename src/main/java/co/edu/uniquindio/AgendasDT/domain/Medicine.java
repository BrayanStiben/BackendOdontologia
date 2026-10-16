package co.edu.uniquindio.AgendasDT.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.util.UUID;

@Getter
@Setter
@Table(name = "medicine")
@Entity
public class Medicine {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private UUID id;

    @Column(name = "medicine_name", nullable = false, length = 150)
    private String medicineName;

    @Column(name = "description", length = 500)
    private String description;

    @Column(name = "stock", nullable = false)
    private Integer stock = 0;

    @Column(name = "minimum_stock", nullable = false)
    private Integer minimumStock = 5;

    @Column(name = "unit_cost", nullable = false)
    private Double unitCost = 0.0;

    public Medicine() {
    }

    public Medicine(String medicineName, String description, Integer stock, Integer minimumStock, Double unitCost) {
        this.medicineName = medicineName;
        this.description = description;
        this.stock = stock;
        this.minimumStock = minimumStock;
        this.unitCost = unitCost;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getMedicineName() {
        return medicineName;
    }

    public void setMedicineName(String medicineName) {
        this.medicineName = medicineName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public Integer getMinimumStock() {
        return minimumStock;
    }

    public void setMinimumStock(Integer minimumStock) {
        this.minimumStock = minimumStock;
    }

    public Double getUnitCost() {
        return unitCost;
    }

    public void setUnitCost(Double unitCost) {
        this.unitCost = unitCost;
    }
}
