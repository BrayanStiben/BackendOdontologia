package co.edu.uniquindio.AgendasDT.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@Table(name = "clinical_history")
@Entity
public class ClinicalHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "patient_id", nullable = false)
    private User patient;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doctor_id")
    private User doctor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "treatment_id")
    private Treatment treatment;

    @Column(name = "record_date", nullable = false)
    private Instant recordDate;

    @Column(name = "tooth_piece", length = 50)
    private String toothPiece;

    @Column(name = "procedure_name", length = 200)
    private String procedureName;

    @Column(name = "diagnosis", length = 1000)
    private String diagnosis;

    @Column(name = "observations", length = 2000)
    private String observations;

    @Column(name = "signed", nullable = false)
    private boolean signed = true;

    // Evaluacion / Anamnesis (Mockup 7)
    @Column(name = "motivo_consulta", length = 500)
    private String motivoConsulta;

    @Column(name = "enfermedad_actual", length = 1000)
    private String enfermedadActual;

    @Column(name = "blood_pressure", length = 20)
    private String bloodPressure;

    @Column(name = "heart_rate", length = 20)
    private String heartRate;

    @Column(name = "temperature", length = 20)
    private String temperature;

    @Column(name = "weight", length = 20)
    private String weight;

    @Column(name = "atm_articulation", length = 100)
    private String atmArticulation;

    @Column(name = "soft_tissues", length = 100)
    private String softTissues;

    @Column(name = "oral_hygiene", length = 50)
    private String oralHygiene;

    @Column(name = "plaque_index", length = 20)
    private String plaqueIndex;

    @Column(name = "dental_calculus")
    private Boolean dentalCalculus = false;

    @Column(name = "food_impaction")
    private Boolean foodImpaction = false;

    @Column(name = "primary_diagnosis", length = 250)
    private String primaryDiagnosis;

    @Column(name = "secondary_diagnosis", length = 250)
    private String secondaryDiagnosis;

    @Column(name = "prognosis", length = 100)
    private String prognosis;

    @Column(name = "suggested_plan", length = 2000)
    private String suggestedPlan;

    public ClinicalHistory() {
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public User getPatient() { return patient; }
    public void setPatient(User patient) { this.patient = patient; }

    public User getDoctor() { return doctor; }
    public void setDoctor(User doctor) { this.doctor = doctor; }

    public Treatment getTreatment() { return treatment; }
    public void setTreatment(Treatment treatment) { this.treatment = treatment; }

    public Instant getRecordDate() { return recordDate; }
    public void setRecordDate(Instant recordDate) { this.recordDate = recordDate; }

    public String getToothPiece() { return toothPiece; }
    public void setToothPiece(String toothPiece) { this.toothPiece = toothPiece; }

    public String getProcedureName() { return procedureName; }
    public void setProcedureName(String procedureName) { this.procedureName = procedureName; }

    public String getDiagnosis() { return diagnosis; }
    public void setDiagnosis(String diagnosis) { this.diagnosis = diagnosis; }

    public String getObservations() { return observations; }
    public void setObservations(String observations) { this.observations = observations; }

    public boolean isSigned() { return signed; }
    public void setSigned(boolean signed) { this.signed = signed; }

    public String getMotivoConsulta() { return motivoConsulta; }
    public void setMotivoConsulta(String motivoConsulta) { this.motivoConsulta = motivoConsulta; }

    public String getEnfermedadActual() { return enfermedadActual; }
    public void setEnfermedadActual(String enfermedadActual) { this.enfermedadActual = enfermedadActual; }

    public String getBloodPressure() { return bloodPressure; }
    public void setBloodPressure(String bloodPressure) { this.bloodPressure = bloodPressure; }

    public String getHeartRate() { return heartRate; }
    public void setHeartRate(String heartRate) { this.heartRate = heartRate; }

    public String getTemperature() { return temperature; }
    public void setTemperature(String temperature) { this.temperature = temperature; }

    public String getWeight() { return weight; }
    public void setWeight(String weight) { this.weight = weight; }

    public String getAtmArticulation() { return atmArticulation; }
    public void setAtmArticulation(String atmArticulation) { this.atmArticulation = atmArticulation; }

    public String getSoftTissues() { return softTissues; }
    public void setSoftTissues(String softTissues) { this.softTissues = softTissues; }

    public String getOralHygiene() { return oralHygiene; }
    public void setOralHygiene(String oralHygiene) { this.oralHygiene = oralHygiene; }

    public String getPlaqueIndex() { return plaqueIndex; }
    public void setPlaqueIndex(String plaqueIndex) { this.plaqueIndex = plaqueIndex; }

    public Boolean getDentalCalculus() { return dentalCalculus; }
    public void setDentalCalculus(Boolean dentalCalculus) { this.dentalCalculus = dentalCalculus; }

    public Boolean getFoodImpaction() { return foodImpaction; }
    public void setFoodImpaction(Boolean foodImpaction) { this.foodImpaction = foodImpaction; }

    public String getPrimaryDiagnosis() { return primaryDiagnosis; }
    public void setPrimaryDiagnosis(String primaryDiagnosis) { this.primaryDiagnosis = primaryDiagnosis; }

    public String getSecondaryDiagnosis() { return secondaryDiagnosis; }
    public void setSecondaryDiagnosis(String secondaryDiagnosis) { this.secondaryDiagnosis = secondaryDiagnosis; }

    public String getPrognosis() { return prognosis; }
    public void setPrognosis(String prognosis) { this.prognosis = prognosis; }

    public String getSuggestedPlan() { return suggestedPlan; }
    public void setSuggestedPlan(String suggestedPlan) { this.suggestedPlan = suggestedPlan; }
}
