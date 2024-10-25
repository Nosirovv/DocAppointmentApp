package uz.devops.service.mapper;

import org.mapstruct.*;
import uz.devops.domain.Patient;
import uz.devops.service.dto.PatientDTO;

/**
 * Mapper for the entity {@link Patient} and its DTO {@link PatientDTO}.
 */
@Mapper(componentModel = "spring")
public interface PatientMapper extends EntityMapper<PatientDTO, Patient> {}
