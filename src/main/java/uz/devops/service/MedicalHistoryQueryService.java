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
import uz.devops.domain.MedicalHistory;
import uz.devops.repository.MedicalHistoryRepository;
import uz.devops.service.criteria.MedicalHistoryCriteria;
import uz.devops.service.dto.MedicalHistoryDTO;
import uz.devops.service.mapper.MedicalHistoryMapper;

/**
 * Service for executing complex queries for {@link MedicalHistory} entities in the database.
 * The main input is a {@link MedicalHistoryCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link MedicalHistoryDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class MedicalHistoryQueryService extends QueryService<MedicalHistory> {

    private static final Logger LOG = LoggerFactory.getLogger(MedicalHistoryQueryService.class);

    private final MedicalHistoryRepository medicalHistoryRepository;

    private final MedicalHistoryMapper medicalHistoryMapper;

    public MedicalHistoryQueryService(MedicalHistoryRepository medicalHistoryRepository, MedicalHistoryMapper medicalHistoryMapper) {
        this.medicalHistoryRepository = medicalHistoryRepository;
        this.medicalHistoryMapper = medicalHistoryMapper;
    }

    /**
     * Return a {@link Page} of {@link MedicalHistoryDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<MedicalHistoryDTO> findByCriteria(MedicalHistoryCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<MedicalHistory> specification = createSpecification(criteria);
        return medicalHistoryRepository.findAll(specification, page).map(medicalHistoryMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(MedicalHistoryCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<MedicalHistory> specification = createSpecification(criteria);
        return medicalHistoryRepository.count(specification);
    }

    /**
     * Function to convert {@link MedicalHistoryCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<MedicalHistory> createSpecification(MedicalHistoryCriteria criteria) {
        Specification<MedicalHistory> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), MedicalHistory_.id));
            }
            if (criteria.getCreatedDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedDate(), MedicalHistory_.createdDate));
            }
            if (criteria.getRecordsId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getRecordsId(), root ->
                        root.join(MedicalHistory_.records, JoinType.LEFT).get(MedicalRecord_.id)
                    )
                );
            }
        }
        return specification;
    }
}
