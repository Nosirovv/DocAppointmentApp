package uz.devops.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import uz.devops.domain.WorkPlan;

public interface WorkPlanRepository extends JpaRepository<WorkPlan, Integer> {
    List<WorkPlan> findByDoctorId(Integer doctorId);

    Optional<WorkPlan> findById(Integer id);
}
