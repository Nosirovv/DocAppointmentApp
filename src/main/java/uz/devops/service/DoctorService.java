package uz.devops.service;

import java.time.*;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.springframework.cglib.core.Local;
import uz.devops.domain.Doctor;
import uz.devops.domain.DoctorWorkSchedule;
import uz.devops.domain.WorkPlan;
import uz.devops.service.dto.DoctorDTO;
import uz.devops.service.dto.DoctorWorkScheduleDto;
import uz.devops.service.dto.TimeSlotDto;
import uz.devops.service.dto.WorkPlanDto;

/**
 * Service Interface for managing {@link uz.devops.domain.Doctor}.
 */
public interface DoctorService {
    /**
     * Save a doctor.
     *
     * @param doctorDTO the entity to save.
     * @return the persisted entity.
     */
    DoctorDTO save(DoctorDTO doctorDTO);

    /**
     * Updates a doctor.
     *
     * @param doctorDTO the entity to update.
     * @return the persisted entity.
     */
    DoctorDTO update(DoctorDTO doctorDTO);

    /**
     * Partially updates a doctor.
     *
     * @param doctorDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<DoctorDTO> partialUpdate(DoctorDTO doctorDTO);

    /**
     * Get the "id" doctor.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<DoctorDTO> findOne(Long id);

    /**
     * Delete the "id" doctor.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    Set<TimeSlotDto> getfreeTime(LocalTime scheduleStart, LocalTime scheduleEnd, Integer doctorId, LocalDate date);

    WorkPlanDto createWorkPlan(WorkPlanDto workPlanDto);

    List<DoctorWorkScheduleDto> generateWeeklyScheduleForDoctor(Long doctorId, LocalDate startDate, LocalDate endDate);

    WorkPlanDto updateWorkPlanTimes(Long id, LocalTime startTime, LocalTime endTime);

    DoctorWorkScheduleDto updateWorkScheduleTimes(Long id, LocalTime startTime, LocalTime endTime);
}
