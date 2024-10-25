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
import uz.devops.domain.Appointment;
import uz.devops.repository.AppointmentRepository;
import uz.devops.service.criteria.AppointmentCriteria;
import uz.devops.service.dto.AppointmentDTO;
import uz.devops.service.mapper.AppointmentMapper;

/**
 * Service for executing complex queries for {@link Appointment} entities in the database.
 * The main input is a {@link AppointmentCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link AppointmentDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class AppointmentQueryService extends QueryService<Appointment> {

    private static final Logger LOG = LoggerFactory.getLogger(AppointmentQueryService.class);

    private final AppointmentRepository appointmentRepository;

    private final AppointmentMapper appointmentMapper;

    public AppointmentQueryService(AppointmentRepository appointmentRepository, AppointmentMapper appointmentMapper) {
        this.appointmentRepository = appointmentRepository;
        this.appointmentMapper = appointmentMapper;
    }

    /**
     * Return a {@link Page} of {@link AppointmentDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<AppointmentDTO> findByCriteria(AppointmentCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Appointment> specification = createSpecification(criteria);
        return appointmentRepository.findAll(specification, page).map(appointmentMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(AppointmentCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<Appointment> specification = createSpecification(criteria);
        return appointmentRepository.count(specification);
    }

    /**
     * Function to convert {@link AppointmentCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Appointment> createSpecification(AppointmentCriteria criteria) {
        Specification<Appointment> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Appointment_.id));
            }
            if (criteria.getAppointmentDateTime() != null) {
                specification = specification.and(
                    buildRangeSpecification(criteria.getAppointmentDateTime(), Appointment_.appointmentDateTime)
                );
            }
            if (criteria.getStatus() != null) {
                specification = specification.and(buildSpecification(criteria.getStatus(), Appointment_.status));
            }
            if (criteria.getDoctorId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getDoctorId(), root -> root.join(Appointment_.doctor, JoinType.LEFT).get(Doctor_.id))
                );
            }
            if (criteria.getPatientId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getPatientId(), root -> root.join(Appointment_.patient, JoinType.LEFT).get(Patient_.id))
                );
            }
        }
        return specification;
    }
}
