package uz.devops.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A MedicalHistory.
 */
@Entity
@Table(name = "medical_history")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class MedicalHistory implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "created_date")
    private LocalDate createdDate;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "history")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "doctor", "history" }, allowSetters = true)
    private Set<MedicalRecord> records = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public MedicalHistory id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getCreatedDate() {
        return this.createdDate;
    }

    public MedicalHistory createdDate(LocalDate createdDate) {
        this.setCreatedDate(createdDate);
        return this;
    }

    public void setCreatedDate(LocalDate createdDate) {
        this.createdDate = createdDate;
    }

    public Set<MedicalRecord> getRecords() {
        return this.records;
    }

    public void setRecords(Set<MedicalRecord> medicalRecords) {
        if (this.records != null) {
            this.records.forEach(i -> i.setHistory(null));
        }
        if (medicalRecords != null) {
            medicalRecords.forEach(i -> i.setHistory(this));
        }
        this.records = medicalRecords;
    }

    public MedicalHistory records(Set<MedicalRecord> medicalRecords) {
        this.setRecords(medicalRecords);
        return this;
    }

    public MedicalHistory addRecords(MedicalRecord medicalRecord) {
        this.records.add(medicalRecord);
        medicalRecord.setHistory(this);
        return this;
    }

    public MedicalHistory removeRecords(MedicalRecord medicalRecord) {
        this.records.remove(medicalRecord);
        medicalRecord.setHistory(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MedicalHistory)) {
            return false;
        }
        return getId() != null && getId().equals(((MedicalHistory) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MedicalHistory{" +
            "id=" + getId() +
            ", createdDate='" + getCreatedDate() + "'" +
            "}";
    }
}
