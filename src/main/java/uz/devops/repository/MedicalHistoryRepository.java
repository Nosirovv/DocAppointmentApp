package uz.devops.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import uz.devops.domain.MedicalHistory;

/**
 * Spring Data JPA repository for the MedicalHistory entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MedicalHistoryRepository extends JpaRepository<MedicalHistory, Long>, JpaSpecificationExecutor<MedicalHistory> {}
