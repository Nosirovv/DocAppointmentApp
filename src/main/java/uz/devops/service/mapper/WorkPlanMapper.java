package uz.devops.service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import uz.devops.domain.Doctor;
import uz.devops.domain.WorkPlan;
import uz.devops.service.dto.DoctorDTO;
import uz.devops.service.dto.WorkPlanDto;

@Mapper(componentModel = "spring")
public interface WorkPlanMapper extends EntityMapper<WorkPlanDto, WorkPlan> {
    @Mapping(target = "doctor", source = "doctorDTO")
    WorkPlan toEntity(WorkPlanDto dto);

    @Mapping(target = "doctorDTO", source = "doctor")
    WorkPlanDto toDto(WorkPlan workPlan);
}
