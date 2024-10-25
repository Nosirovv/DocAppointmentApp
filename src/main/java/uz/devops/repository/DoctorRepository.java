package uz.devops.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import uz.devops.domain.Doctor;

/**
 * Spring Data JPA repository for the Doctor entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Long>, JpaSpecificationExecutor<Doctor> {}
