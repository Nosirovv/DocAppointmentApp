package uz.devops.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.devops.domain.DoctorWorkSchedule;

public interface DoctorWorkScheduleRepository extends JpaRepository<DoctorWorkSchedule, Integer> {}
