package uz.devops.service;

import jakarta.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;
import uz.devops.domain.*; // for static metamodels
import uz.devops.domain.MedicalRecord;
import uz.devops.repository.MedicalRecordRepository;
import uz.devops.service.criteria.MedicalRecordCriteria;
import uz.devops.service.dto.MedicalRecordDTO;
import uz.devops.service.mapper.MedicalRecordMapper;

/**
 * Service for executing complex queries for {@link MedicalRecord} entities in the database.
 * The main input is a {@link MedicalRecordCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link MedicalRecordDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class MedicalRecordQueryService extends QueryService<MedicalRecord> {

    private static final Logger LOG = LoggerFactory.getLogger(MedicalRecordQueryService.class);

    private final MedicalRecordRepository medicalRecordRepository;

    private final MedicalRecordMapper medicalRecordMapper;

    public MedicalRecordQueryService(MedicalRecordRepository medicalRecordRepository, MedicalRecordMapper medicalRecordMapper) {
        this.medicalRecordRepository = medicalRecordRepository;
        this.medicalRecordMapper = medicalRecordMapper;
    }

    /**
     * Return a {@link Page} of {@link MedicalRecordDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<MedicalRecordDTO> findByCriteria(MedicalRecordCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<MedicalRecord> specification = createSpecification(criteria);
        return medicalRecordRepository.findAll(specification, page).map(medicalRecordMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(MedicalRecordCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<MedicalRecord> specification = createSpecification(criteria);
        return medicalRecordRepository.count(specification);
    }

    /**
     * Function to convert {@link MedicalRecordCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<MedicalRecord> createSpecification(MedicalRecordCriteria criteria) {
        Specification<MedicalRecord> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), MedicalRecord_.id));
            }
            if (criteria.getCreatedDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedDate(), MedicalRecord_.createdDate));
            }
            if (criteria.getSicknessInfo() != null) {
                specification = specification.and(buildStringSpecification(criteria.getSicknessInfo(), MedicalRecord_.sicknessInfo));
            }
            if (criteria.getTreatmentInto() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTreatmentInto(), MedicalRecord_.treatmentInto));
            }
            if (criteria.getDoctorId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getDoctorId(), root -> root.join(MedicalRecord_.doctor, JoinType.LEFT).get(Doctor_.id))
                );
            }
            if (criteria.getHistoryId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getHistoryId(), root ->
                        root.join(MedicalRecord_.history, JoinType.LEFT).get(MedicalHistory_.id)
                    )
                );
            }
        }
        return specification;
    }
}
