package uz.devops.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import uz.devops.domain.enumeration.Specialization;

/**
 * A Doctor.
 */
@Entity
@Table(name = "doctor")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Doctor implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "specialization")
    private Specialization specialization;

    @Column(name = "available_from")
    private ZonedDateTime availableFrom;

    @Column(name = "available_to")
    private ZonedDateTime availableTo;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "doctor")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "doctor", "patient" }, allowSetters = true)
    private Set<Appointment> appointments = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "doctor")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "doctor", "history" }, allowSetters = true)
    private Set<MedicalRecord> records = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Doctor id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Doctor name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Specialization getSpecialization() {
        return this.specialization;
    }

    public Doctor specialization(Specialization specialization) {
        this.setSpecialization(specialization);
        return this;
    }

    public void setSpecialization(Specialization specialization) {
        this.specialization = specialization;
    }

    public ZonedDateTime getAvailableFrom() {
        return this.availableFrom;
    }

    public Doctor availableFrom(ZonedDateTime availableFrom) {
        this.setAvailableFrom(availableFrom);
        return this;
    }

    public void setAvailableFrom(ZonedDateTime availableFrom) {
        this.availableFrom = availableFrom;
    }

    public ZonedDateTime getAvailableTo() {
        return this.availableTo;
    }

    public Doctor availableTo(ZonedDateTime availableTo) {
        this.setAvailableTo(availableTo);
        return this;
    }

    public void setAvailableTo(ZonedDateTime availableTo) {
        this.availableTo = availableTo;
    }

    public Set<Appointment> getAppointments() {
        return this.appointments;
    }

    public void setAppointments(Set<Appointment> appointments) {
        if (this.appointments != null) {
            this.appointments.forEach(i -> i.setDoctor(null));
        }
        if (appointments != null) {
            appointments.forEach(i -> i.setDoctor(this));
        }
        this.appointments = appointments;
    }

    public Doctor appointments(Set<Appointment> appointments) {
        this.setAppointments(appointments);
        return this;
    }

    public Doctor addAppointments(Appointment appointment) {
        this.appointments.add(appointment);
        appointment.setDoctor(this);
        return this;
    }

    public Doctor removeAppointments(Appointment appointment) {
        this.appointments.remove(appointment);
        appointment.setDoctor(null);
        return this;
    }

    public Set<MedicalRecord> getRecords() {
        return this.records;
    }

    public void setRecords(Set<MedicalRecord> medicalRecords) {
        if (this.records != null) {
            this.records.forEach(i -> i.setDoctor(null));
        }
        if (medicalRecords != null) {
            medicalRecords.forEach(i -> i.setDoctor(this));
        }
        this.records = medicalRecords;
    }

    public Doctor records(Set<MedicalRecord> medicalRecords) {
        this.setRecords(medicalRecords);
        return this;
    }

    public Doctor addRecords(MedicalRecord medicalRecord) {
        this.records.add(medicalRecord);
        medicalRecord.setDoctor(this);
        return this;
    }

    public Doctor removeRecords(MedicalRecord medicalRecord) {
        this.records.remove(medicalRecord);
        medicalRecord.setDoctor(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Doctor)) {
            return false;
        }
        return getId() != null && getId().equals(((Doctor) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Doctor{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", specialization='" + getSpecialization() + "'" +
            ", availableFrom='" + getAvailableFrom() + "'" +
            ", availableTo='" + getAvailableTo() + "'" +
            "}";
    }
}
