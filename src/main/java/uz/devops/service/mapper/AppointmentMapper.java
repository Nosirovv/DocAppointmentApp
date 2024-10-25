package uz.devops.service.mapper;

import org.mapstruct.*;
import uz.devops.domain.Appointment;
import uz.devops.domain.Doctor;
import uz.devops.domain.Patient;
import uz.devops.service.dto.AppointmentDTO;
import uz.devops.service.dto.DoctorDTO;
import uz.devops.service.dto.PatientDTO;

/**
 * Mapper for the entity {@link Appointment} and its DTO {@link AppointmentDTO}.
 */
@Mapper(componentModel = "spring")
public interface AppointmentMapper extends EntityMapper<AppointmentDTO, Appointment> {
    @Mapping(target = "doctor", source = "doctor", qualifiedByName = "doctorId")
    @Mapping(target = "patient", source = "patient", qualifiedByName = "patientId")
    AppointmentDTO toDto(Appointment s);

    @Named("doctorId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    DoctorDTO toDtoDoctorId(Doctor doctor);

    @Named("patientId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    PatientDTO toDtoPatientId(Patient patient);
}
