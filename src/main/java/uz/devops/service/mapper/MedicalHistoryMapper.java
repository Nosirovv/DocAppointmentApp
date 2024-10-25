package uz.devops.service.mapper;

import org.mapstruct.*;
import uz.devops.domain.MedicalHistory;
import uz.devops.service.dto.MedicalHistoryDTO;

/**
 * Mapper for the entity {@link MedicalHistory} and its DTO {@link MedicalHistoryDTO}.
 */
@Mapper(componentModel = "spring")
public interface MedicalHistoryMapper extends EntityMapper<MedicalHistoryDTO, MedicalHistory> {}
