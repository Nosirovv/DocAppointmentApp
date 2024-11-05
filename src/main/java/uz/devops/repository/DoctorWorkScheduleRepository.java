package uz.devops.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import uz.devops.domain.DoctorWorkSchedule;

/**
 * Spring Data JPA repository for the DoctorWorkSchedule entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DoctorWorkScheduleRepository extends JpaRepository<DoctorWorkSchedule, Long> {}
