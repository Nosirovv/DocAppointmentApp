package uz.devops.service.mapper;

import org.mapstruct.*;
import org.springframework.security.core.userdetails.User;
import uz.devops.domain.Doctor;
import uz.devops.service.dto.DoctorDTO;

/**
 * Mapper for the entity {@link Doctor} and its DTO {@link DoctorDTO}.
 */
@Mapper(componentModel = "spring", uses = { UserMapper.class })
public interface DoctorMapper extends EntityMapper<DoctorDTO, Doctor> {
    @Mapping(target = "user", source = "adminUserDTO")
    Doctor toEntity(DoctorDTO dto);

    @Mapping(target = "adminUserDTO", source = "user")
    DoctorDTO toDto(Doctor doctor);
}
