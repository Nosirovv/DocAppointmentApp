package uz.devops.service.impl;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.devops.domain.MedicalRecord;
import uz.devops.repository.MedicalRecordRepository;
import uz.devops.service.MedicalRecordService;
import uz.devops.service.dto.MedicalRecordDTO;
import uz.devops.service.mapper.MedicalRecordMapper;

/**
 * Service Implementation for managing {@link uz.devops.domain.MedicalRecord}.
 */
@Service
@Transactional
public class MedicalRecordServiceImpl implements MedicalRecordService {

    private static final Logger LOG = LoggerFactory.getLogger(MedicalRecordServiceImpl.class);

    private final MedicalRecordRepository medicalRecordRepository;

    private final MedicalRecordMapper medicalRecordMapper;

    public MedicalRecordServiceImpl(MedicalRecordRepository medicalRecordRepository, MedicalRecordMapper medicalRecordMapper) {
        this.medicalRecordRepository = medicalRecordRepository;
        this.medicalRecordMapper = medicalRecordMapper;
    }

    @Override
    public MedicalRecordDTO save(MedicalRecordDTO medicalRecordDTO) {
        LOG.debug("Request to save MedicalRecord : {}", medicalRecordDTO);
        MedicalRecord medicalRecord = medicalRecordMapper.toEntity(medicalRecordDTO);
        medicalRecord = medicalRecordRepository.save(medicalRecord);
        return medicalRecordMapper.toDto(medicalRecord);
    }

    @Override
    public MedicalRecordDTO update(MedicalRecordDTO medicalRecordDTO) {
        LOG.debug("Request to update MedicalRecord : {}", medicalRecordDTO);
        MedicalRecord medicalRecord = medicalRecordMapper.toEntity(medicalRecordDTO);
        medicalRecord = medicalRecordRepository.save(medicalRecord);
        return medicalRecordMapper.toDto(medicalRecord);
    }

    @Override
    public Optional<MedicalRecordDTO> partialUpdate(MedicalRecordDTO medicalRecordDTO) {
        LOG.debug("Request to partially update MedicalRecord : {}", medicalRecordDTO);

        return medicalRecordRepository
            .findById(medicalRecordDTO.getId())
            .map(existingMedicalRecord -> {
                medicalRecordMapper.partialUpdate(existingMedicalRecord, medicalRecordDTO);

                return existingMedicalRecord;
            })
            .map(medicalRecordRepository::save)
            .map(medicalRecordMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<MedicalRecordDTO> findOne(Long id) {
        LOG.debug("Request to get MedicalRecord : {}", id);
        return medicalRecordRepository.findById(id).map(medicalRecordMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete MedicalRecord : {}", id);
        medicalRecordRepository.deleteById(id);
    }
}
