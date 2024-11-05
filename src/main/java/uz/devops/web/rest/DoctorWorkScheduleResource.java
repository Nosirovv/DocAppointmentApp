package uz.devops.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;
import uz.devops.domain.DoctorWorkSchedule;
import uz.devops.repository.DoctorWorkScheduleRepository;
import uz.devops.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link uz.devops.domain.DoctorWorkSchedule}.
 */
@RestController
@RequestMapping("/api/doctor-work-schedules")
@Transactional
public class DoctorWorkScheduleResource {

    private static final Logger LOG = LoggerFactory.getLogger(DoctorWorkScheduleResource.class);

    private static final String ENTITY_NAME = "doctorWorkSchedule";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DoctorWorkScheduleRepository doctorWorkScheduleRepository;

    public DoctorWorkScheduleResource(DoctorWorkScheduleRepository doctorWorkScheduleRepository) {
        this.doctorWorkScheduleRepository = doctorWorkScheduleRepository;
    }

    /**
     * {@code POST  /doctor-work-schedules} : Create a new doctorWorkSchedule.
     *
     * @param doctorWorkSchedule the doctorWorkSchedule to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new doctorWorkSchedule, or with status {@code 400 (Bad Request)} if the doctorWorkSchedule has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<DoctorWorkSchedule> createDoctorWorkSchedule(@RequestBody DoctorWorkSchedule doctorWorkSchedule)
        throws URISyntaxException {
        LOG.debug("REST request to save DoctorWorkSchedule : {}", doctorWorkSchedule);
        if (doctorWorkSchedule.getId() != null) {
            throw new BadRequestAlertException("A new doctorWorkSchedule cannot already have an ID", ENTITY_NAME, "idexists");
        }
        doctorWorkSchedule = doctorWorkScheduleRepository.save(doctorWorkSchedule);
        return ResponseEntity.created(new URI("/api/doctor-work-schedules/" + doctorWorkSchedule.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, doctorWorkSchedule.getId().toString()))
            .body(doctorWorkSchedule);
    }

    /**
     * {@code GET  /doctor-work-schedules} : get all the doctorWorkSchedules.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of doctorWorkSchedules in body.
     */
    @GetMapping("")
    public List<DoctorWorkSchedule> getAllDoctorWorkSchedules() {
        LOG.debug("REST request to get all DoctorWorkSchedules");
        return doctorWorkScheduleRepository.findAll();
    }

    /**
     * {@code GET  /doctor-work-schedules/:id} : get the "id" doctorWorkSchedule.
     *
     * @param id the id of the doctorWorkSchedule to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the doctorWorkSchedule, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<DoctorWorkSchedule> getDoctorWorkSchedule(@PathVariable("id") Long id) {
        LOG.debug("REST request to get DoctorWorkSchedule : {}", id);
        Optional<DoctorWorkSchedule> doctorWorkSchedule = doctorWorkScheduleRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(doctorWorkSchedule);
    }

    /**
     * {@code DELETE  /doctor-work-schedules/:id} : delete the "id" doctorWorkSchedule.
     *
     * @param id the id of the doctorWorkSchedule to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDoctorWorkSchedule(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete DoctorWorkSchedule : {}", id);
        doctorWorkScheduleRepository.deleteById(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
