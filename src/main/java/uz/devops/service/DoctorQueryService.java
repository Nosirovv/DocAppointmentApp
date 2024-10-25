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
import uz.devops.domain.Doctor;
import uz.devops.repository.DoctorRepository;
import uz.devops.service.criteria.DoctorCriteria;
import uz.devops.service.dto.DoctorDTO;
import uz.devops.service.mapper.DoctorMapper;

/**
 * Service for executing complex queries for {@link Doctor} entities in the database.
 * The main input is a {@link DoctorCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link DoctorDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class DoctorQueryService extends QueryService<Doctor> {

    private static final Logger LOG = LoggerFactory.getLogger(DoctorQueryService.class);

    private final DoctorRepository doctorRepository;

    private final DoctorMapper doctorMapper;

    public DoctorQueryService(DoctorRepository doctorRepository, DoctorMapper doctorMapper) {
        this.doctorRepository = doctorRepository;
        this.doctorMapper = doctorMapper;
    }

    /**
     * Return a {@link Page} of {@link DoctorDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<DoctorDTO> findByCriteria(DoctorCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Doctor> specification = createSpecification(criteria);
        return doctorRepository.findAll(specification, page).map(doctorMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(DoctorCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<Doctor> specification = createSpecification(criteria);
        return doctorRepository.count(specification);
    }

    /**
     * Function to convert {@link DoctorCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Doctor> createSpecification(DoctorCriteria criteria) {
        Specification<Doctor> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Doctor_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), Doctor_.name));
            }
            if (criteria.getSpecialization() != null) {
                specification = specification.and(buildSpecification(criteria.getSpecialization(), Doctor_.specialization));
            }
            if (criteria.getAvailableFrom() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getAvailableFrom(), Doctor_.availableFrom));
            }
            if (criteria.getAvailableTo() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getAvailableTo(), Doctor_.availableTo));
            }
            if (criteria.getAppointmentsId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getAppointmentsId(), root ->
                        root.join(Doctor_.appointments, JoinType.LEFT).get(Appointment_.id)
                    )
                );
            }
            if (criteria.getRecordsId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getRecordsId(), root -> root.join(Doctor_.records, JoinType.LEFT).get(MedicalRecord_.id))
                );
            }
        }
        return specification;
    }
}
