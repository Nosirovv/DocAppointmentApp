package uz.devops.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static uz.devops.domain.MedicalRecordAsserts.*;
import static uz.devops.web.rest.TestUtil.createUpdateProxyForBean;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import uz.devops.IntegrationTest;
import uz.devops.domain.Doctor;
import uz.devops.domain.MedicalHistory;
import uz.devops.domain.MedicalRecord;
import uz.devops.repository.MedicalRecordRepository;
import uz.devops.service.dto.MedicalRecordDTO;
import uz.devops.service.mapper.MedicalRecordMapper;

/**
 * Integration tests for the {@link MedicalRecordResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class MedicalRecordResourceIT {

    private static final LocalDate DEFAULT_CREATED_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CREATED_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_CREATED_DATE = LocalDate.ofEpochDay(-1L);

    private static final String DEFAULT_SICKNESS_INFO = "AAAAAAAAAA";
    private static final String UPDATED_SICKNESS_INFO = "BBBBBBBBBB";

    private static final String DEFAULT_TREATMENT_INTO = "AAAAAAAAAA";
    private static final String UPDATED_TREATMENT_INTO = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/medical-records";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private MedicalRecordRepository medicalRecordRepository;

    @Autowired
    private MedicalRecordMapper medicalRecordMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMedicalRecordMockMvc;

    private MedicalRecord medicalRecord;

    private MedicalRecord insertedMedicalRecord;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MedicalRecord createEntity() {
        return new MedicalRecord()
            .createdDate(DEFAULT_CREATED_DATE)
            .sicknessInfo(DEFAULT_SICKNESS_INFO)
            .treatmentInto(DEFAULT_TREATMENT_INTO);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MedicalRecord createUpdatedEntity() {
        return new MedicalRecord()
            .createdDate(UPDATED_CREATED_DATE)
            .sicknessInfo(UPDATED_SICKNESS_INFO)
            .treatmentInto(UPDATED_TREATMENT_INTO);
    }

    @BeforeEach
    public void initTest() {
        medicalRecord = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedMedicalRecord != null) {
            medicalRecordRepository.delete(insertedMedicalRecord);
            insertedMedicalRecord = null;
        }
    }

    @Test
    @Transactional
    void createMedicalRecord() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the MedicalRecord
        MedicalRecordDTO medicalRecordDTO = medicalRecordMapper.toDto(medicalRecord);
        var returnedMedicalRecordDTO = om.readValue(
            restMedicalRecordMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(medicalRecordDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            MedicalRecordDTO.class
        );

        // Validate the MedicalRecord in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedMedicalRecord = medicalRecordMapper.toEntity(returnedMedicalRecordDTO);
        assertMedicalRecordUpdatableFieldsEquals(returnedMedicalRecord, getPersistedMedicalRecord(returnedMedicalRecord));

        insertedMedicalRecord = returnedMedicalRecord;
    }

    @Test
    @Transactional
    void createMedicalRecordWithExistingId() throws Exception {
        // Create the MedicalRecord with an existing ID
        medicalRecord.setId(1L);
        MedicalRecordDTO medicalRecordDTO = medicalRecordMapper.toDto(medicalRecord);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restMedicalRecordMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(medicalRecordDTO)))
            .andExpect(status().isBadRequest());

        // Validate the MedicalRecord in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllMedicalRecords() throws Exception {
        // Initialize the database
        insertedMedicalRecord = medicalRecordRepository.saveAndFlush(medicalRecord);

        // Get all the medicalRecordList
        restMedicalRecordMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(medicalRecord.getId().intValue())))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].sicknessInfo").value(hasItem(DEFAULT_SICKNESS_INFO)))
            .andExpect(jsonPath("$.[*].treatmentInto").value(hasItem(DEFAULT_TREATMENT_INTO)));
    }

    @Test
    @Transactional
    void getMedicalRecord() throws Exception {
        // Initialize the database
        insertedMedicalRecord = medicalRecordRepository.saveAndFlush(medicalRecord);

        // Get the medicalRecord
        restMedicalRecordMockMvc
            .perform(get(ENTITY_API_URL_ID, medicalRecord.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(medicalRecord.getId().intValue()))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()))
            .andExpect(jsonPath("$.sicknessInfo").value(DEFAULT_SICKNESS_INFO))
            .andExpect(jsonPath("$.treatmentInto").value(DEFAULT_TREATMENT_INTO));
    }

    @Test
    @Transactional
    void getMedicalRecordsByIdFiltering() throws Exception {
        // Initialize the database
        insertedMedicalRecord = medicalRecordRepository.saveAndFlush(medicalRecord);

        Long id = medicalRecord.getId();

        defaultMedicalRecordFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultMedicalRecordFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultMedicalRecordFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllMedicalRecordsByCreatedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedMedicalRecord = medicalRecordRepository.saveAndFlush(medicalRecord);

        // Get all the medicalRecordList where createdDate equals to
        defaultMedicalRecordFiltering("createdDate.equals=" + DEFAULT_CREATED_DATE, "createdDate.equals=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    void getAllMedicalRecordsByCreatedDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedMedicalRecord = medicalRecordRepository.saveAndFlush(medicalRecord);

        // Get all the medicalRecordList where createdDate in
        defaultMedicalRecordFiltering(
            "createdDate.in=" + DEFAULT_CREATED_DATE + "," + UPDATED_CREATED_DATE,
            "createdDate.in=" + UPDATED_CREATED_DATE
        );
    }

    @Test
    @Transactional
    void getAllMedicalRecordsByCreatedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedMedicalRecord = medicalRecordRepository.saveAndFlush(medicalRecord);

        // Get all the medicalRecordList where createdDate is not null
        defaultMedicalRecordFiltering("createdDate.specified=true", "createdDate.specified=false");
    }

    @Test
    @Transactional
    void getAllMedicalRecordsByCreatedDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedMedicalRecord = medicalRecordRepository.saveAndFlush(medicalRecord);

        // Get all the medicalRecordList where createdDate is greater than or equal to
        defaultMedicalRecordFiltering(
            "createdDate.greaterThanOrEqual=" + DEFAULT_CREATED_DATE,
            "createdDate.greaterThanOrEqual=" + UPDATED_CREATED_DATE
        );
    }

    @Test
    @Transactional
    void getAllMedicalRecordsByCreatedDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedMedicalRecord = medicalRecordRepository.saveAndFlush(medicalRecord);

        // Get all the medicalRecordList where createdDate is less than or equal to
        defaultMedicalRecordFiltering(
            "createdDate.lessThanOrEqual=" + DEFAULT_CREATED_DATE,
            "createdDate.lessThanOrEqual=" + SMALLER_CREATED_DATE
        );
    }

    @Test
    @Transactional
    void getAllMedicalRecordsByCreatedDateIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedMedicalRecord = medicalRecordRepository.saveAndFlush(medicalRecord);

        // Get all the medicalRecordList where createdDate is less than
        defaultMedicalRecordFiltering("createdDate.lessThan=" + UPDATED_CREATED_DATE, "createdDate.lessThan=" + DEFAULT_CREATED_DATE);
    }

    @Test
    @Transactional
    void getAllMedicalRecordsByCreatedDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedMedicalRecord = medicalRecordRepository.saveAndFlush(medicalRecord);

        // Get all the medicalRecordList where createdDate is greater than
        defaultMedicalRecordFiltering("createdDate.greaterThan=" + SMALLER_CREATED_DATE, "createdDate.greaterThan=" + DEFAULT_CREATED_DATE);
    }

    @Test
    @Transactional
    void getAllMedicalRecordsBySicknessInfoIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedMedicalRecord = medicalRecordRepository.saveAndFlush(medicalRecord);

        // Get all the medicalRecordList where sicknessInfo equals to
        defaultMedicalRecordFiltering("sicknessInfo.equals=" + DEFAULT_SICKNESS_INFO, "sicknessInfo.equals=" + UPDATED_SICKNESS_INFO);
    }

    @Test
    @Transactional
    void getAllMedicalRecordsBySicknessInfoIsInShouldWork() throws Exception {
        // Initialize the database
        insertedMedicalRecord = medicalRecordRepository.saveAndFlush(medicalRecord);

        // Get all the medicalRecordList where sicknessInfo in
        defaultMedicalRecordFiltering(
            "sicknessInfo.in=" + DEFAULT_SICKNESS_INFO + "," + UPDATED_SICKNESS_INFO,
            "sicknessInfo.in=" + UPDATED_SICKNESS_INFO
        );
    }

    @Test
    @Transactional
    void getAllMedicalRecordsBySicknessInfoIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedMedicalRecord = medicalRecordRepository.saveAndFlush(medicalRecord);

        // Get all the medicalRecordList where sicknessInfo is not null
        defaultMedicalRecordFiltering("sicknessInfo.specified=true", "sicknessInfo.specified=false");
    }

    @Test
    @Transactional
    void getAllMedicalRecordsBySicknessInfoContainsSomething() throws Exception {
        // Initialize the database
        insertedMedicalRecord = medicalRecordRepository.saveAndFlush(medicalRecord);

        // Get all the medicalRecordList where sicknessInfo contains
        defaultMedicalRecordFiltering("sicknessInfo.contains=" + DEFAULT_SICKNESS_INFO, "sicknessInfo.contains=" + UPDATED_SICKNESS_INFO);
    }

    @Test
    @Transactional
    void getAllMedicalRecordsBySicknessInfoNotContainsSomething() throws Exception {
        // Initialize the database
        insertedMedicalRecord = medicalRecordRepository.saveAndFlush(medicalRecord);

        // Get all the medicalRecordList where sicknessInfo does not contain
        defaultMedicalRecordFiltering(
            "sicknessInfo.doesNotContain=" + UPDATED_SICKNESS_INFO,
            "sicknessInfo.doesNotContain=" + DEFAULT_SICKNESS_INFO
        );
    }

    @Test
    @Transactional
    void getAllMedicalRecordsByTreatmentIntoIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedMedicalRecord = medicalRecordRepository.saveAndFlush(medicalRecord);

        // Get all the medicalRecordList where treatmentInto equals to
        defaultMedicalRecordFiltering("treatmentInto.equals=" + DEFAULT_TREATMENT_INTO, "treatmentInto.equals=" + UPDATED_TREATMENT_INTO);
    }

    @Test
    @Transactional
    void getAllMedicalRecordsByTreatmentIntoIsInShouldWork() throws Exception {
        // Initialize the database
        insertedMedicalRecord = medicalRecordRepository.saveAndFlush(medicalRecord);

        // Get all the medicalRecordList where treatmentInto in
        defaultMedicalRecordFiltering(
            "treatmentInto.in=" + DEFAULT_TREATMENT_INTO + "," + UPDATED_TREATMENT_INTO,
            "treatmentInto.in=" + UPDATED_TREATMENT_INTO
        );
    }

    @Test
    @Transactional
    void getAllMedicalRecordsByTreatmentIntoIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedMedicalRecord = medicalRecordRepository.saveAndFlush(medicalRecord);

        // Get all the medicalRecordList where treatmentInto is not null
        defaultMedicalRecordFiltering("treatmentInto.specified=true", "treatmentInto.specified=false");
    }

    @Test
    @Transactional
    void getAllMedicalRecordsByTreatmentIntoContainsSomething() throws Exception {
        // Initialize the database
        insertedMedicalRecord = medicalRecordRepository.saveAndFlush(medicalRecord);

        // Get all the medicalRecordList where treatmentInto contains
        defaultMedicalRecordFiltering(
            "treatmentInto.contains=" + DEFAULT_TREATMENT_INTO,
            "treatmentInto.contains=" + UPDATED_TREATMENT_INTO
        );
    }

    @Test
    @Transactional
    void getAllMedicalRecordsByTreatmentIntoNotContainsSomething() throws Exception {
        // Initialize the database
        insertedMedicalRecord = medicalRecordRepository.saveAndFlush(medicalRecord);

        // Get all the medicalRecordList where treatmentInto does not contain
        defaultMedicalRecordFiltering(
            "treatmentInto.doesNotContain=" + UPDATED_TREATMENT_INTO,
            "treatmentInto.doesNotContain=" + DEFAULT_TREATMENT_INTO
        );
    }

    @Test
    @Transactional
    void getAllMedicalRecordsByDoctorIsEqualToSomething() throws Exception {
        Doctor doctor;
        if (TestUtil.findAll(em, Doctor.class).isEmpty()) {
            medicalRecordRepository.saveAndFlush(medicalRecord);
            doctor = DoctorResourceIT.createEntity();
        } else {
            doctor = TestUtil.findAll(em, Doctor.class).get(0);
        }
        em.persist(doctor);
        em.flush();
        medicalRecord.setDoctor(doctor);
        medicalRecordRepository.saveAndFlush(medicalRecord);
        Long doctorId = doctor.getId();
        // Get all the medicalRecordList where doctor equals to doctorId
        defaultMedicalRecordShouldBeFound("doctorId.equals=" + doctorId);

        // Get all the medicalRecordList where doctor equals to (doctorId + 1)
        defaultMedicalRecordShouldNotBeFound("doctorId.equals=" + (doctorId + 1));
    }

    @Test
    @Transactional
    void getAllMedicalRecordsByHistoryIsEqualToSomething() throws Exception {
        MedicalHistory history;
        if (TestUtil.findAll(em, MedicalHistory.class).isEmpty()) {
            medicalRecordRepository.saveAndFlush(medicalRecord);
            history = MedicalHistoryResourceIT.createEntity();
        } else {
            history = TestUtil.findAll(em, MedicalHistory.class).get(0);
        }
        em.persist(history);
        em.flush();
        medicalRecord.setHistory(history);
        medicalRecordRepository.saveAndFlush(medicalRecord);
        Long historyId = history.getId();
        // Get all the medicalRecordList where history equals to historyId
        defaultMedicalRecordShouldBeFound("historyId.equals=" + historyId);

        // Get all the medicalRecordList where history equals to (historyId + 1)
        defaultMedicalRecordShouldNotBeFound("historyId.equals=" + (historyId + 1));
    }

    private void defaultMedicalRecordFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultMedicalRecordShouldBeFound(shouldBeFound);
        defaultMedicalRecordShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultMedicalRecordShouldBeFound(String filter) throws Exception {
        restMedicalRecordMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(medicalRecord.getId().intValue())))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].sicknessInfo").value(hasItem(DEFAULT_SICKNESS_INFO)))
            .andExpect(jsonPath("$.[*].treatmentInto").value(hasItem(DEFAULT_TREATMENT_INTO)));

        // Check, that the count call also returns 1
        restMedicalRecordMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultMedicalRecordShouldNotBeFound(String filter) throws Exception {
        restMedicalRecordMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restMedicalRecordMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingMedicalRecord() throws Exception {
        // Get the medicalRecord
        restMedicalRecordMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingMedicalRecord() throws Exception {
        // Initialize the database
        insertedMedicalRecord = medicalRecordRepository.saveAndFlush(medicalRecord);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the medicalRecord
        MedicalRecord updatedMedicalRecord = medicalRecordRepository.findById(medicalRecord.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedMedicalRecord are not directly saved in db
        em.detach(updatedMedicalRecord);
        updatedMedicalRecord.createdDate(UPDATED_CREATED_DATE).sicknessInfo(UPDATED_SICKNESS_INFO).treatmentInto(UPDATED_TREATMENT_INTO);
        MedicalRecordDTO medicalRecordDTO = medicalRecordMapper.toDto(updatedMedicalRecord);

        restMedicalRecordMockMvc
            .perform(
                put(ENTITY_API_URL_ID, medicalRecordDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(medicalRecordDTO))
            )
            .andExpect(status().isOk());

        // Validate the MedicalRecord in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedMedicalRecordToMatchAllProperties(updatedMedicalRecord);
    }

    @Test
    @Transactional
    void putNonExistingMedicalRecord() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        medicalRecord.setId(longCount.incrementAndGet());

        // Create the MedicalRecord
        MedicalRecordDTO medicalRecordDTO = medicalRecordMapper.toDto(medicalRecord);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMedicalRecordMockMvc
            .perform(
                put(ENTITY_API_URL_ID, medicalRecordDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(medicalRecordDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MedicalRecord in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchMedicalRecord() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        medicalRecord.setId(longCount.incrementAndGet());

        // Create the MedicalRecord
        MedicalRecordDTO medicalRecordDTO = medicalRecordMapper.toDto(medicalRecord);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMedicalRecordMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(medicalRecordDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MedicalRecord in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamMedicalRecord() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        medicalRecord.setId(longCount.incrementAndGet());

        // Create the MedicalRecord
        MedicalRecordDTO medicalRecordDTO = medicalRecordMapper.toDto(medicalRecord);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMedicalRecordMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(medicalRecordDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the MedicalRecord in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateMedicalRecordWithPatch() throws Exception {
        // Initialize the database
        insertedMedicalRecord = medicalRecordRepository.saveAndFlush(medicalRecord);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the medicalRecord using partial update
        MedicalRecord partialUpdatedMedicalRecord = new MedicalRecord();
        partialUpdatedMedicalRecord.setId(medicalRecord.getId());

        restMedicalRecordMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMedicalRecord.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedMedicalRecord))
            )
            .andExpect(status().isOk());

        // Validate the MedicalRecord in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertMedicalRecordUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedMedicalRecord, medicalRecord),
            getPersistedMedicalRecord(medicalRecord)
        );
    }

    @Test
    @Transactional
    void fullUpdateMedicalRecordWithPatch() throws Exception {
        // Initialize the database
        insertedMedicalRecord = medicalRecordRepository.saveAndFlush(medicalRecord);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the medicalRecord using partial update
        MedicalRecord partialUpdatedMedicalRecord = new MedicalRecord();
        partialUpdatedMedicalRecord.setId(medicalRecord.getId());

        partialUpdatedMedicalRecord
            .createdDate(UPDATED_CREATED_DATE)
            .sicknessInfo(UPDATED_SICKNESS_INFO)
            .treatmentInto(UPDATED_TREATMENT_INTO);

        restMedicalRecordMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMedicalRecord.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedMedicalRecord))
            )
            .andExpect(status().isOk());

        // Validate the MedicalRecord in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertMedicalRecordUpdatableFieldsEquals(partialUpdatedMedicalRecord, getPersistedMedicalRecord(partialUpdatedMedicalRecord));
    }

    @Test
    @Transactional
    void patchNonExistingMedicalRecord() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        medicalRecord.setId(longCount.incrementAndGet());

        // Create the MedicalRecord
        MedicalRecordDTO medicalRecordDTO = medicalRecordMapper.toDto(medicalRecord);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMedicalRecordMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, medicalRecordDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(medicalRecordDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MedicalRecord in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchMedicalRecord() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        medicalRecord.setId(longCount.incrementAndGet());

        // Create the MedicalRecord
        MedicalRecordDTO medicalRecordDTO = medicalRecordMapper.toDto(medicalRecord);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMedicalRecordMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(medicalRecordDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MedicalRecord in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamMedicalRecord() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        medicalRecord.setId(longCount.incrementAndGet());

        // Create the MedicalRecord
        MedicalRecordDTO medicalRecordDTO = medicalRecordMapper.toDto(medicalRecord);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMedicalRecordMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(medicalRecordDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the MedicalRecord in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteMedicalRecord() throws Exception {
        // Initialize the database
        insertedMedicalRecord = medicalRecordRepository.saveAndFlush(medicalRecord);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the medicalRecord
        restMedicalRecordMockMvc
            .perform(delete(ENTITY_API_URL_ID, medicalRecord.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return medicalRecordRepository.count();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected MedicalRecord getPersistedMedicalRecord(MedicalRecord medicalRecord) {
        return medicalRecordRepository.findById(medicalRecord.getId()).orElseThrow();
    }

    protected void assertPersistedMedicalRecordToMatchAllProperties(MedicalRecord expectedMedicalRecord) {
        assertMedicalRecordAllPropertiesEquals(expectedMedicalRecord, getPersistedMedicalRecord(expectedMedicalRecord));
    }

    protected void assertPersistedMedicalRecordToMatchUpdatableProperties(MedicalRecord expectedMedicalRecord) {
        assertMedicalRecordAllUpdatablePropertiesEquals(expectedMedicalRecord, getPersistedMedicalRecord(expectedMedicalRecord));
    }
}
