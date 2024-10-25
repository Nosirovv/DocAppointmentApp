package uz.devops.service.impl;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.devops.domain.MedicalHistory;
import uz.devops.repository.MedicalHistoryRepository;
import uz.devops.service.MedicalHistoryService;
import uz.devops.service.dto.MedicalHistoryDTO;
import uz.devops.service.mapper.MedicalHistoryMapper;

/**
 * Service Implementation for managing {@link uz.devops.domain.MedicalHistory}.
 */
@Service
@Transactional
public class MedicalHistoryServiceImpl implements MedicalHistoryService {

    private static final Logger LOG = LoggerFactory.getLogger(MedicalHistoryServiceImpl.class);

    private final MedicalHistoryRepository medicalHistoryRepository;

    private final MedicalHistoryMapper medicalHistoryMapper;

    public MedicalHistoryServiceImpl(MedicalHistoryRepository medicalHistoryRepository, MedicalHistoryMapper medicalHistoryMapper) {
        this.medicalHistoryRepository = medicalHistoryRepository;
        this.medicalHistoryMapper = medicalHistoryMapper;
    }

    @Override
    public MedicalHistoryDTO save(MedicalHistoryDTO medicalHistoryDTO) {
        LOG.debug("Request to save MedicalHistory : {}", medicalHistoryDTO);
        MedicalHistory medicalHistory = medicalHistoryMapper.toEntity(medicalHistoryDTO);
        medicalHistory = medicalHistoryRepository.save(medicalHistory);
        return medicalHistoryMapper.toDto(medicalHistory);
    }

    @Override
    public MedicalHistoryDTO update(MedicalHistoryDTO medicalHistoryDTO) {
        LOG.debug("Request to update MedicalHistory : {}", medicalHistoryDTO);
        MedicalHistory medicalHistory = medicalHistoryMapper.toEntity(medicalHistoryDTO);
        medicalHistory = medicalHistoryRepository.save(medicalHistory);
        return medicalHistoryMapper.toDto(medicalHistory);
    }

    @Override
    public Optional<MedicalHistoryDTO> partialUpdate(MedicalHistoryDTO medicalHistoryDTO) {
        LOG.debug("Request to partially update MedicalHistory : {}", medicalHistoryDTO);

        return medicalHistoryRepository
            .findById(medicalHistoryDTO.getId())
            .map(existingMedicalHistory -> {
                medicalHistoryMapper.partialUpdate(existingMedicalHistory, medicalHistoryDTO);

                return existingMedicalHistory;
            })
            .map(medicalHistoryRepository::save)
            .map(medicalHistoryMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<MedicalHistoryDTO> findOne(Long id) {
        LOG.debug("Request to get MedicalHistory : {}", id);
        return medicalHistoryRepository.findById(id).map(medicalHistoryMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete MedicalHistory : {}", id);
        medicalHistoryRepository.deleteById(id);
    }
}
