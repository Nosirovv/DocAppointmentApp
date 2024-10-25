package uz.devops.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A MedicalRecord.
 */
@Entity
@Table(name = "medical_record")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class MedicalRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "created_date")
    private LocalDate createdDate;

    @Column(name = "sickness_info")
    private String sicknessInfo;

    @Column(name = "treatment_into")
    private String treatmentInto;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "appointments", "records" }, allowSetters = true)
    private Doctor doctor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "records" }, allowSetters = true)
    private MedicalHistory history;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public MedicalRecord id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getCreatedDate() {
        return this.createdDate;
    }

    public MedicalRecord createdDate(LocalDate createdDate) {
        this.setCreatedDate(createdDate);
        return this;
    }

    public void setCreatedDate(LocalDate createdDate) {
        this.createdDate = createdDate;
    }

    public String getSicknessInfo() {
        return this.sicknessInfo;
    }

    public MedicalRecord sicknessInfo(String sicknessInfo) {
        this.setSicknessInfo(sicknessInfo);
        return this;
    }

    public void setSicknessInfo(String sicknessInfo) {
        this.sicknessInfo = sicknessInfo;
    }

    public String getTreatmentInto() {
        return this.treatmentInto;
    }

    public MedicalRecord treatmentInto(String treatmentInto) {
        this.setTreatmentInto(treatmentInto);
        return this;
    }

    public void setTreatmentInto(String treatmentInto) {
        this.treatmentInto = treatmentInto;
    }

    public Doctor getDoctor() {
        return this.doctor;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }

    public MedicalRecord doctor(Doctor doctor) {
        this.setDoctor(doctor);
        return this;
    }

    public MedicalHistory getHistory() {
        return this.history;
    }

    public void setHistory(MedicalHistory medicalHistory) {
        this.history = medicalHistory;
    }

    public MedicalRecord history(MedicalHistory medicalHistory) {
        this.setHistory(medicalHistory);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MedicalRecord)) {
            return false;
        }
        return getId() != null && getId().equals(((MedicalRecord) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MedicalRecord{" +
            "id=" + getId() +
            ", createdDate='" + getCreatedDate() + "'" +
            ", sicknessInfo='" + getSicknessInfo() + "'" +
            ", treatmentInto='" + getTreatmentInto() + "'" +
            "}";
    }
}
