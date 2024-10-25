package uz.devops.service.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link uz.devops.domain.MedicalRecord} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class MedicalRecordDTO implements Serializable {

    private Long id;

    private LocalDate createdDate;

    private String sicknessInfo;

    private String treatmentInto;

    private DoctorDTO doctor;

    private MedicalHistoryDTO history;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDate createdDate) {
        this.createdDate = createdDate;
    }

    public String getSicknessInfo() {
        return sicknessInfo;
    }

    public void setSicknessInfo(String sicknessInfo) {
        this.sicknessInfo = sicknessInfo;
    }

    public String getTreatmentInto() {
        return treatmentInto;
    }

    public void setTreatmentInto(String treatmentInto) {
        this.treatmentInto = treatmentInto;
    }

    public DoctorDTO getDoctor() {
        return doctor;
    }

    public void setDoctor(DoctorDTO doctor) {
        this.doctor = doctor;
    }

    public MedicalHistoryDTO getHistory() {
        return history;
    }

    public void setHistory(MedicalHistoryDTO history) {
        this.history = history;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MedicalRecordDTO)) {
            return false;
        }

        MedicalRecordDTO medicalRecordDTO = (MedicalRecordDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, medicalRecordDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MedicalRecordDTO{" +
            "id=" + getId() +
            ", createdDate='" + getCreatedDate() + "'" +
            ", sicknessInfo='" + getSicknessInfo() + "'" +
            ", treatmentInto='" + getTreatmentInto() + "'" +
            ", doctor=" + getDoctor() +
            ", history=" + getHistory() +
            "}";
    }
}
