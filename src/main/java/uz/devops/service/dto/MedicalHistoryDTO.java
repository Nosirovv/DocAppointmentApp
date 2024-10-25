package uz.devops.service.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link uz.devops.domain.MedicalHistory} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class MedicalHistoryDTO implements Serializable {

    private Long id;

    private LocalDate createdDate;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MedicalHistoryDTO)) {
            return false;
        }

        MedicalHistoryDTO medicalHistoryDTO = (MedicalHistoryDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, medicalHistoryDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MedicalHistoryDTO{" +
            "id=" + getId() +
            ", createdDate='" + getCreatedDate() + "'" +
            "}";
    }
}
