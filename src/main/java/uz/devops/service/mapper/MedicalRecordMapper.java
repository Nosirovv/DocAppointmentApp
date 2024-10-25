package uz.devops.service.mapper;

import org.mapstruct.*;
import uz.devops.domain.Doctor;
import uz.devops.domain.MedicalHistory;
import uz.devops.domain.MedicalRecord;
import uz.devops.service.dto.DoctorDTO;
import uz.devops.service.dto.MedicalHistoryDTO;
import uz.devops.service.dto.MedicalRecordDTO;

/**
 * Mapper for the entity {@link MedicalRecord} and its DTO {@link MedicalRecordDTO}.
 */
@Mapper(componentModel = "spring")
public interface MedicalRecordMapper extends EntityMapper<MedicalRecordDTO, MedicalRecord> {
    @Mapping(target = "doctor", source = "doctor", qualifiedByName = "doctorId")
    @Mapping(target = "history", source = "history", qualifiedByName = "medicalHistoryId")
    MedicalRecordDTO toDto(MedicalRecord s);

    @Named("doctorId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    DoctorDTO toDtoDoctorId(Doctor doctor);

    @Named("medicalHistoryId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    MedicalHistoryDTO toDtoMedicalHistoryId(MedicalHistory medicalHistory);
}
