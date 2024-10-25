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
import uz.devops.domain.Patient;
import uz.devops.repository.PatientRepository;
import uz.devops.service.criteria.PatientCriteria;
import uz.devops.service.dto.PatientDTO;
import uz.devops.service.mapper.PatientMapper;

/**
 * Service for executing complex queries for {@link Patient} entities in the database.
 * The main input is a {@link PatientCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link PatientDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class PatientQueryService extends QueryService<Patient> {

    private static final Logger LOG = LoggerFactory.getLogger(PatientQueryService.class);

    private final PatientRepository patientRepository;

    private final PatientMapper patientMapper;

    public PatientQueryService(PatientRepository patientRepository, PatientMapper patientMapper) {
        this.patientRepository = patientRepository;
        this.patientMapper = patientMapper;
    }

    /**
     * Return a {@link Page} of {@link PatientDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<PatientDTO> findByCriteria(PatientCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Patient> specification = createSpecification(criteria);
        return patientRepository.findAll(specification, page).map(patientMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(PatientCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<Patient> specification = createSpecification(criteria);
        return patientRepository.count(specification);
    }

    /**
     * Function to convert {@link PatientCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Patient> createSpecification(PatientCriteria criteria) {
        Specification<Patient> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Patient_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), Patient_.name));
            }
            if (criteria.getEmail() != null) {
                specification = specification.and(buildStringSpecification(criteria.getEmail(), Patient_.email));
            }
            if (criteria.getDateOfBirth() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDateOfBirth(), Patient_.dateOfBirth));
            }
            if (criteria.getAppointmentsId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getAppointmentsId(), root ->
                        root.join(Patient_.appointments, JoinType.LEFT).get(Appointment_.id)
                    )
                );
            }
        }
        return specification;
    }
}
