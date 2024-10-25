package uz.devops.service;

import java.util.Optional;
import uz.devops.service.dto.MedicalHistoryDTO;

/**
 * Service Interface for managing {@link uz.devops.domain.MedicalHistory}.
 */
public interface MedicalHistoryService {
    /**
     * Save a medicalHistory.
     *
     * @param medicalHistoryDTO the entity to save.
     * @return the persisted entity.
     */
    MedicalHistoryDTO save(MedicalHistoryDTO medicalHistoryDTO);

    /**
     * Updates a medicalHistory.
     *
     * @param medicalHistoryDTO the entity to update.
     * @return the persisted entity.
     */
    MedicalHistoryDTO update(MedicalHistoryDTO medicalHistoryDTO);

    /**
     * Partially updates a medicalHistory.
     *
     * @param medicalHistoryDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<MedicalHistoryDTO> partialUpdate(MedicalHistoryDTO medicalHistoryDTO);

    /**
     * Get the "id" medicalHistory.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<MedicalHistoryDTO> findOne(Long id);

    /**
     * Delete the "id" medicalHistory.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
