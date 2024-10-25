package uz.devops.service;

import java.util.Optional;
import uz.devops.service.dto.MedicalRecordDTO;

/**
 * Service Interface for managing {@link uz.devops.domain.MedicalRecord}.
 */
public interface MedicalRecordService {
    /**
     * Save a medicalRecord.
     *
     * @param medicalRecordDTO the entity to save.
     * @return the persisted entity.
     */
    MedicalRecordDTO save(MedicalRecordDTO medicalRecordDTO);

    /**
     * Updates a medicalRecord.
     *
     * @param medicalRecordDTO the entity to update.
     * @return the persisted entity.
     */
    MedicalRecordDTO update(MedicalRecordDTO medicalRecordDTO);

    /**
     * Partially updates a medicalRecord.
     *
     * @param medicalRecordDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<MedicalRecordDTO> partialUpdate(MedicalRecordDTO medicalRecordDTO);

    /**
     * Get the "id" medicalRecord.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<MedicalRecordDTO> findOne(Long id);

    /**
     * Delete the "id" medicalRecord.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
