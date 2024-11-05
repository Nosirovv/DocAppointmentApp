package uz.devops.service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import uz.devops.domain.DoctorWorkSchedule;
import uz.devops.domain.WorkPlan;
import uz.devops.service.dto.DoctorWorkScheduleDto;
import uz.devops.service.dto.WorkPlanDto;

@Mapper(componentModel = "spring")
public interface DoctorWorkScheduleMapper extends EntityMapper<DoctorWorkScheduleDto, DoctorWorkSchedule> {
    @Mapping(target = "doctorId", source = "doctorIdDto")
    DoctorWorkSchedule toEntity(DoctorWorkScheduleDto dto);

    @Mapping(target = "doctorIdDto", source = "doctorId")
    DoctorWorkScheduleDto toDto(DoctorWorkSchedule doctorWorkSchedule);
}
