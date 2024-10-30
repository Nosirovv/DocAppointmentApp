package uz.devops.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import uz.devops.domain.enumeration.AppointmentStatus;

/**
 * A DTO for the {@link uz.devops.domain.Appointment} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AppointmentDTO implements Serializable {

    private Long id;

    @NotNull
    private Instant appointmentStartTime;

    @NotNull
    private Instant appointmentEndTime;

    private AppointmentStatus status;

    private DoctorDTO doctor;

    private PatientDTO patient;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getAppointmentStartTime() {
        return appointmentStartTime;
    }

    public Instant getAppointmentEndTime() {
        return appointmentEndTime;
    }

    public void setAppointmentStartTime(Instant appointmentStartTime) {
        this.appointmentStartTime = appointmentStartTime;
    }

    public void setAppointmentEndTime(Instant appointmentEndTime) {
        this.appointmentEndTime = appointmentEndTime;
    }

    public AppointmentStatus getStatus() {
        return status;
    }

    public void setStatus(AppointmentStatus status) {
        this.status = status;
    }

    public DoctorDTO getDoctor() {
        return doctor;
    }

    public void setDoctor(DoctorDTO doctor) {
        this.doctor = doctor;
    }

    public PatientDTO getPatient() {
        return patient;
    }

    public void setPatient(PatientDTO patient) {
        this.patient = patient;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AppointmentDTO)) {
            return false;
        }

        AppointmentDTO appointmentDTO = (AppointmentDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, appointmentDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AppointmentDTO{" +
            "id=" + getId() +
            ", appointmentStartTime='" + getAppointmentStartTime() + "'" +
            ", status='" + getStatus() + "'" +
            ", doctor=" + getDoctor() +
            ", patient=" + getPatient() +
            "}";
    }
}
