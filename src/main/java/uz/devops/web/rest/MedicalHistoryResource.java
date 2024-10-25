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
import uz.devops.repository.MedicalHistoryRepository;
import uz.devops.service.MedicalHistoryQueryService;
import uz.devops.service.MedicalHistoryService;
import uz.devops.service.criteria.MedicalHistoryCriteria;
import uz.devops.service.dto.MedicalHistoryDTO;
import uz.devops.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link uz.devops.domain.MedicalHistory}.
 */
@RestController
@RequestMapping("/api/medical-histories")
public class MedicalHistoryResource {

    private static final Logger LOG = LoggerFactory.getLogger(MedicalHistoryResource.class);

    private static final String ENTITY_NAME = "medicalHistory";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MedicalHistoryService medicalHistoryService;

    private final MedicalHistoryRepository medicalHistoryRepository;

    private final MedicalHistoryQueryService medicalHistoryQueryService;

    public MedicalHistoryResource(
        MedicalHistoryService medicalHistoryService,
        MedicalHistoryRepository medicalHistoryRepository,
        MedicalHistoryQueryService medicalHistoryQueryService
    ) {
        this.medicalHistoryService = medicalHistoryService;
        this.medicalHistoryRepository = medicalHistoryRepository;
        this.medicalHistoryQueryService = medicalHistoryQueryService;
    }

    /**
     * {@code POST  /medical-histories} : Create a new medicalHistory.
     *
     * @param medicalHistoryDTO the medicalHistoryDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new medicalHistoryDTO, or with status {@code 400 (Bad Request)} if the medicalHistory has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<MedicalHistoryDTO> createMedicalHistory(@RequestBody MedicalHistoryDTO medicalHistoryDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save MedicalHistory : {}", medicalHistoryDTO);
        if (medicalHistoryDTO.getId() != null) {
            throw new BadRequestAlertException("A new medicalHistory cannot already have an ID", ENTITY_NAME, "idexists");
        }
        medicalHistoryDTO = medicalHistoryService.save(medicalHistoryDTO);
        return ResponseEntity.created(new URI("/api/medical-histories/" + medicalHistoryDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, medicalHistoryDTO.getId().toString()))
            .body(medicalHistoryDTO);
    }

    /**
     * {@code PUT  /medical-histories/:id} : Updates an existing medicalHistory.
     *
     * @param id the id of the medicalHistoryDTO to save.
     * @param medicalHistoryDTO the medicalHistoryDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated medicalHistoryDTO,
     * or with status {@code 400 (Bad Request)} if the medicalHistoryDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the medicalHistoryDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<MedicalHistoryDTO> updateMedicalHistory(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody MedicalHistoryDTO medicalHistoryDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update MedicalHistory : {}, {}", id, medicalHistoryDTO);
        if (medicalHistoryDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, medicalHistoryDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!medicalHistoryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        medicalHistoryDTO = medicalHistoryService.update(medicalHistoryDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, medicalHistoryDTO.getId().toString()))
            .body(medicalHistoryDTO);
    }

    /**
     * {@code PATCH  /medical-histories/:id} : Partial updates given fields of an existing medicalHistory, field will ignore if it is null
     *
     * @param id the id of the medicalHistoryDTO to save.
     * @param medicalHistoryDTO the medicalHistoryDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated medicalHistoryDTO,
     * or with status {@code 400 (Bad Request)} if the medicalHistoryDTO is not valid,
     * or with status {@code 404 (Not Found)} if the medicalHistoryDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the medicalHistoryDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<MedicalHistoryDTO> partialUpdateMedicalHistory(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody MedicalHistoryDTO medicalHistoryDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update MedicalHistory partially : {}, {}", id, medicalHistoryDTO);
        if (medicalHistoryDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, medicalHistoryDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!medicalHistoryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<MedicalHistoryDTO> result = medicalHistoryService.partialUpdate(medicalHistoryDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, medicalHistoryDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /medical-histories} : get all the medicalHistories.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of medicalHistories in body.
     */
    @GetMapping("")
    public ResponseEntity<List<MedicalHistoryDTO>> getAllMedicalHistories(
        MedicalHistoryCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get MedicalHistories by criteria: {}", criteria);

        Page<MedicalHistoryDTO> page = medicalHistoryQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /medical-histories/count} : count all the medicalHistories.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countMedicalHistories(MedicalHistoryCriteria criteria) {
        LOG.debug("REST request to count MedicalHistories by criteria: {}", criteria);
        return ResponseEntity.ok().body(medicalHistoryQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /medical-histories/:id} : get the "id" medicalHistory.
     *
     * @param id the id of the medicalHistoryDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the medicalHistoryDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<MedicalHistoryDTO> getMedicalHistory(@PathVariable("id") Long id) {
        LOG.debug("REST request to get MedicalHistory : {}", id);
        Optional<MedicalHistoryDTO> medicalHistoryDTO = medicalHistoryService.findOne(id);
        return ResponseUtil.wrapOrNotFound(medicalHistoryDTO);
    }

    /**
     * {@code DELETE  /medical-histories/:id} : delete the "id" medicalHistory.
     *
     * @param id the id of the medicalHistoryDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMedicalHistory(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete MedicalHistory : {}", id);
        medicalHistoryService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
