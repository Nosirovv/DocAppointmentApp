package uz.devops.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import uz.devops.domain.MedicalRecord;

/**
 * Spring Data JPA repository for the MedicalRecord entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MedicalRecordRepository extends JpaRepository<MedicalRecord, Long>, JpaSpecificationExecutor<MedicalRecord> {}
