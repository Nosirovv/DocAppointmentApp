package uz.devops.service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Optional;
import java.util.Set;
import uz.devops.domain.Doctor;
import uz.devops.service.dto.DoctorDTO;
import uz.devops.service.dto.TimeSlotDto;

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

    Set<TimeSlotDto> freeTime(LocalTime scheduleStart, LocalTime scheduleEnd, Integer doctorId);
}
