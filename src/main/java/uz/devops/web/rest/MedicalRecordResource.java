package uz.devops.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;
import uz.devops.repository.MedicalRecordRepository;
import uz.devops.service.MedicalRecordQueryService;
import uz.devops.service.MedicalRecordService;
import uz.devops.service.criteria.MedicalRecordCriteria;
import uz.devops.service.dto.MedicalRecordDTO;
import uz.devops.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link uz.devops.domain.MedicalRecord}.
 */
@RestController
@RequestMapping("/api/medical-records")
public class MedicalRecordResource {

    private static final Logger LOG = LoggerFactory.getLogger(MedicalRecordResource.class);

    private static final String ENTITY_NAME = "medicalRecord";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MedicalRecordService medicalRecordService;

    private final MedicalRecordRepository medicalRecordRepository;

    private final MedicalRecordQueryService medicalRecordQueryService;

    public MedicalRecordResource(
        MedicalRecordService medicalRecordService,
        MedicalRecordRepository medicalRecordRepository,
        MedicalRecordQueryService medicalRecordQueryService
    ) {
        this.medicalRecordService = medicalRecordService;
        this.medicalRecordRepository = medicalRecordRepository;
        this.medicalRecordQueryService = medicalRecordQueryService;
    }

    /**
     * {@code POST  /medical-records} : Create a new medicalRecord.
     *
     * @param medicalRecordDTO the medicalRecordDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new medicalRecordDTO, or with status {@code 400 (Bad Request)} if the medicalRecord has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<MedicalRecordDTO> createMedicalRecord(@RequestBody MedicalRecordDTO medicalRecordDTO) throws URISyntaxException {
        LOG.debug("REST request to save MedicalRecord : {}", medicalRecordDTO);
        if (medicalRecordDTO.getId() != null) {
            throw new BadRequestAlertException("A new medicalRecord cannot already have an ID", ENTITY_NAME, "idexists");
        }
        medicalRecordDTO = medicalRecordService.save(medicalRecordDTO);
        return ResponseEntity.created(new URI("/api/medical-records/" + medicalRecordDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, medicalRecordDTO.getId().toString()))
            .body(medicalRecordDTO);
    }

    /**
     * {@code PUT  /medical-records/:id} : Updates an existing medicalRecord.
     *
     * @param id the id of the medicalRecordDTO to save.
     * @param medicalRecordDTO the medicalRecordDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated medicalRecordDTO,
     * or with status {@code 400 (Bad Request)} if the medicalRecordDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the medicalRecordDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<MedicalRecordDTO> updateMedicalRecord(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody MedicalRecordDTO medicalRecordDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update MedicalRecord : {}, {}", id, medicalRecordDTO);
        if (medicalRecordDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, medicalRecordDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!medicalRecordRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        medicalRecordDTO = medicalRecordService.update(medicalRecordDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, medicalRecordDTO.getId().toString()))
            .body(medicalRecordDTO);
    }

    /**
     * {@code PATCH  /medical-records/:id} : Partial updates given fields of an existing medicalRecord, field will ignore if it is null
     *
     * @param id the id of the medicalRecordDTO to save.
     * @param medicalRecordDTO the medicalRecordDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated medicalRecordDTO,
     * or with status {@code 400 (Bad Request)} if the medicalRecordDTO is not valid,
     * or with status {@code 404 (Not Found)} if the medicalRecordDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the medicalRecordDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<MedicalRecordDTO> partialUpdateMedicalRecord(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody MedicalRecordDTO medicalRecordDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update MedicalRecord partially : {}, {}", id, medicalRecordDTO);
        if (medicalRecordDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, medicalRecordDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!medicalRecordRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<MedicalRecordDTO> result = medicalRecordService.partialUpdate(medicalRecordDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, medicalRecordDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /medical-records} : get all the medicalRecords.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of medicalRecords in body.
     */
    @GetMapping("")
    public ResponseEntity<List<MedicalRecordDTO>> getAllMedicalRecords(
        MedicalRecordCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get MedicalRecords by criteria: {}", criteria);

        Page<MedicalRecordDTO> page = medicalRecordQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /medical-records/count} : count all the medicalRecords.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countMedicalRecords(MedicalRecordCriteria criteria) {
        LOG.debug("REST request to count MedicalRecords by criteria: {}", criteria);
        return ResponseEntity.ok().body(medicalRecordQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /medical-records/:id} : get the "id" medicalRecord.
     *
     * @param id the id of the medicalRecordDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the medicalRecordDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<MedicalRecordDTO> getMedicalRecord(@PathVariable("id") Long id) {
        LOG.debug("REST request to get MedicalRecord : {}", id);
        Optional<MedicalRecordDTO> medicalRecordDTO = medicalRecordService.findOne(id);
        return ResponseUtil.wrapOrNotFound(medicalRecordDTO);
    }

    /**
     * {@code DELETE  /medical-records/:id} : delete the "id" medicalRecord.
     *
     * @param id the id of the medicalRecordDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMedicalRecord(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete MedicalRecord : {}", id);
        medicalRecordService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
