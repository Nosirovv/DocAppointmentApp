package uz.devops.service.mapper;

import org.mapstruct.*;
import uz.devops.domain.Doctor;
import uz.devops.service.dto.DoctorDTO;

/**
 * Mapper for the entity {@link Doctor} and its DTO {@link DoctorDTO}.
 */
@Mapper(componentModel = "spring")
public interface DoctorMapper extends EntityMapper<DoctorDTO, Doctor> {}
