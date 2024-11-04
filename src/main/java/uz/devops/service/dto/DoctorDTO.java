package uz.devops.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalTime;
import java.util.Objects;
import uz.devops.domain.enumeration.Specialization;

/**
 * A DTO for the {@link uz.devops.domain.Doctor} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class DoctorDTO implements Serializable {

    private Long id;

    @NotNull
    private String name;

    private Specialization specialization;

    private AdminUserDTO adminUserDTO;

    public AdminUserDTO getAdminUserDTO() {
        return adminUserDTO;
    }

    public DoctorDTO() {}

    public DoctorDTO(Long id, String name, Specialization specialization, AdminUserDTO adminUserDTO) {
        this.id = id;
        this.name = name;
        this.specialization = specialization;
        this.adminUserDTO = adminUserDTO;
    }

    public void setAdminUserDTO(AdminUserDTO adminUserDTO) {
        this.adminUserDTO = adminUserDTO;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Specialization getSpecialization() {
        return specialization;
    }

    public void setSpecialization(Specialization specialization) {
        this.specialization = specialization;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DoctorDTO)) {
            return false;
        }

        DoctorDTO doctorDTO = (DoctorDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, doctorDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DoctorDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", specialization='" + getSpecialization() + "'" +
            "}";
    }
}
