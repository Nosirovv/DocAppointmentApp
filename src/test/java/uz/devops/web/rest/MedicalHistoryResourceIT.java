package uz.devops.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static uz.devops.domain.MedicalHistoryAsserts.*;
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
import uz.devops.domain.MedicalHistory;
import uz.devops.repository.MedicalHistoryRepository;
import uz.devops.service.dto.MedicalHistoryDTO;
import uz.devops.service.mapper.MedicalHistoryMapper;

/**
 * Integration tests for the {@link MedicalHistoryResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class MedicalHistoryResourceIT {

    private static final LocalDate DEFAULT_CREATED_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CREATED_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_CREATED_DATE = LocalDate.ofEpochDay(-1L);

    private static final String ENTITY_API_URL = "/api/medical-histories";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private MedicalHistoryRepository medicalHistoryRepository;

    @Autowired
    private MedicalHistoryMapper medicalHistoryMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMedicalHistoryMockMvc;

    private MedicalHistory medicalHistory;

    private MedicalHistory insertedMedicalHistory;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MedicalHistory createEntity() {
        return new MedicalHistory().createdDate(DEFAULT_CREATED_DATE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MedicalHistory createUpdatedEntity() {
        return new MedicalHistory().createdDate(UPDATED_CREATED_DATE);
    }

    @BeforeEach
    public void initTest() {
        medicalHistory = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedMedicalHistory != null) {
            medicalHistoryRepository.delete(insertedMedicalHistory);
            insertedMedicalHistory = null;
        }
    }

    @Test
    @Transactional
    void createMedicalHistory() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the MedicalHistory
        MedicalHistoryDTO medicalHistoryDTO = medicalHistoryMapper.toDto(medicalHistory);
        var returnedMedicalHistoryDTO = om.readValue(
            restMedicalHistoryMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(medicalHistoryDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            MedicalHistoryDTO.class
        );

        // Validate the MedicalHistory in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedMedicalHistory = medicalHistoryMapper.toEntity(returnedMedicalHistoryDTO);
        assertMedicalHistoryUpdatableFieldsEquals(returnedMedicalHistory, getPersistedMedicalHistory(returnedMedicalHistory));

        insertedMedicalHistory = returnedMedicalHistory;
    }

    @Test
    @Transactional
    void createMedicalHistoryWithExistingId() throws Exception {
        // Create the MedicalHistory with an existing ID
        medicalHistory.setId(1L);
        MedicalHistoryDTO medicalHistoryDTO = medicalHistoryMapper.toDto(medicalHistory);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restMedicalHistoryMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(medicalHistoryDTO)))
            .andExpect(status().isBadRequest());

        // Validate the MedicalHistory in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllMedicalHistories() throws Exception {
        // Initialize the database
        insertedMedicalHistory = medicalHistoryRepository.saveAndFlush(medicalHistory);

        // Get all the medicalHistoryList
        restMedicalHistoryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(medicalHistory.getId().intValue())))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())));
    }

    @Test
    @Transactional
    void getMedicalHistory() throws Exception {
        // Initialize the database
        insertedMedicalHistory = medicalHistoryRepository.saveAndFlush(medicalHistory);

        // Get the medicalHistory
        restMedicalHistoryMockMvc
            .perform(get(ENTITY_API_URL_ID, medicalHistory.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(medicalHistory.getId().intValue()))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()));
    }

    @Test
    @Transactional
    void getMedicalHistoriesByIdFiltering() throws Exception {
        // Initialize the database
        insertedMedicalHistory = medicalHistoryRepository.saveAndFlush(medicalHistory);

        Long id = medicalHistory.getId();

        defaultMedicalHistoryFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultMedicalHistoryFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultMedicalHistoryFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllMedicalHistoriesByCreatedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedMedicalHistory = medicalHistoryRepository.saveAndFlush(medicalHistory);

        // Get all the medicalHistoryList where createdDate equals to
        defaultMedicalHistoryFiltering("createdDate.equals=" + DEFAULT_CREATED_DATE, "createdDate.equals=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    void getAllMedicalHistoriesByCreatedDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedMedicalHistory = medicalHistoryRepository.saveAndFlush(medicalHistory);

        // Get all the medicalHistoryList where createdDate in
        defaultMedicalHistoryFiltering(
            "createdDate.in=" + DEFAULT_CREATED_DATE + "," + UPDATED_CREATED_DATE,
            "createdDate.in=" + UPDATED_CREATED_DATE
        );
    }

    @Test
    @Transactional
    void getAllMedicalHistoriesByCreatedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedMedicalHistory = medicalHistoryRepository.saveAndFlush(medicalHistory);

        // Get all the medicalHistoryList where createdDate is not null
        defaultMedicalHistoryFiltering("createdDate.specified=true", "createdDate.specified=false");
    }

    @Test
    @Transactional
    void getAllMedicalHistoriesByCreatedDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedMedicalHistory = medicalHistoryRepository.saveAndFlush(medicalHistory);

        // Get all the medicalHistoryList where createdDate is greater than or equal to
        defaultMedicalHistoryFiltering(
            "createdDate.greaterThanOrEqual=" + DEFAULT_CREATED_DATE,
            "createdDate.greaterThanOrEqual=" + UPDATED_CREATED_DATE
        );
    }

    @Test
    @Transactional
    void getAllMedicalHistoriesByCreatedDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedMedicalHistory = medicalHistoryRepository.saveAndFlush(medicalHistory);

        // Get all the medicalHistoryList where createdDate is less than or equal to
        defaultMedicalHistoryFiltering(
            "createdDate.lessThanOrEqual=" + DEFAULT_CREATED_DATE,
            "createdDate.lessThanOrEqual=" + SMALLER_CREATED_DATE
        );
    }

    @Test
    @Transactional
    void getAllMedicalHistoriesByCreatedDateIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedMedicalHistory = medicalHistoryRepository.saveAndFlush(medicalHistory);

        // Get all the medicalHistoryList where createdDate is less than
        defaultMedicalHistoryFiltering("createdDate.lessThan=" + UPDATED_CREATED_DATE, "createdDate.lessThan=" + DEFAULT_CREATED_DATE);
    }

    @Test
    @Transactional
    void getAllMedicalHistoriesByCreatedDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedMedicalHistory = medicalHistoryRepository.saveAndFlush(medicalHistory);

        // Get all the medicalHistoryList where createdDate is greater than
        defaultMedicalHistoryFiltering(
            "createdDate.greaterThan=" + SMALLER_CREATED_DATE,
            "createdDate.greaterThan=" + DEFAULT_CREATED_DATE
        );
    }

    private void defaultMedicalHistoryFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultMedicalHistoryShouldBeFound(shouldBeFound);
        defaultMedicalHistoryShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultMedicalHistoryShouldBeFound(String filter) throws Exception {
        restMedicalHistoryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(medicalHistory.getId().intValue())))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())));

        // Check, that the count call also returns 1
        restMedicalHistoryMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultMedicalHistoryShouldNotBeFound(String filter) throws Exception {
        restMedicalHistoryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restMedicalHistoryMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingMedicalHistory() throws Exception {
        // Get the medicalHistory
        restMedicalHistoryMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingMedicalHistory() throws Exception {
        // Initialize the database
        insertedMedicalHistory = medicalHistoryRepository.saveAndFlush(medicalHistory);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the medicalHistory
        MedicalHistory updatedMedicalHistory = medicalHistoryRepository.findById(medicalHistory.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedMedicalHistory are not directly saved in db
        em.detach(updatedMedicalHistory);
        updatedMedicalHistory.createdDate(UPDATED_CREATED_DATE);
        MedicalHistoryDTO medicalHistoryDTO = medicalHistoryMapper.toDto(updatedMedicalHistory);

        restMedicalHistoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, medicalHistoryDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(medicalHistoryDTO))
            )
            .andExpect(status().isOk());

        // Validate the MedicalHistory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedMedicalHistoryToMatchAllProperties(updatedMedicalHistory);
    }

    @Test
    @Transactional
    void putNonExistingMedicalHistory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        medicalHistory.setId(longCount.incrementAndGet());

        // Create the MedicalHistory
        MedicalHistoryDTO medicalHistoryDTO = medicalHistoryMapper.toDto(medicalHistory);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMedicalHistoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, medicalHistoryDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(medicalHistoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MedicalHistory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchMedicalHistory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        medicalHistory.setId(longCount.incrementAndGet());

        // Create the MedicalHistory
        MedicalHistoryDTO medicalHistoryDTO = medicalHistoryMapper.toDto(medicalHistory);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMedicalHistoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(medicalHistoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MedicalHistory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamMedicalHistory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        medicalHistory.setId(longCount.incrementAndGet());

        // Create the MedicalHistory
        MedicalHistoryDTO medicalHistoryDTO = medicalHistoryMapper.toDto(medicalHistory);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMedicalHistoryMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(medicalHistoryDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the MedicalHistory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateMedicalHistoryWithPatch() throws Exception {
        // Initialize the database
        insertedMedicalHistory = medicalHistoryRepository.saveAndFlush(medicalHistory);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the medicalHistory using partial update
        MedicalHistory partialUpdatedMedicalHistory = new MedicalHistory();
        partialUpdatedMedicalHistory.setId(medicalHistory.getId());

        restMedicalHistoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMedicalHistory.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedMedicalHistory))
            )
            .andExpect(status().isOk());

        // Validate the MedicalHistory in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertMedicalHistoryUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedMedicalHistory, medicalHistory),
            getPersistedMedicalHistory(medicalHistory)
        );
    }

    @Test
    @Transactional
    void fullUpdateMedicalHistoryWithPatch() throws Exception {
        // Initialize the database
        insertedMedicalHistory = medicalHistoryRepository.saveAndFlush(medicalHistory);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the medicalHistory using partial update
        MedicalHistory partialUpdatedMedicalHistory = new MedicalHistory();
        partialUpdatedMedicalHistory.setId(medicalHistory.getId());

        partialUpdatedMedicalHistory.createdDate(UPDATED_CREATED_DATE);

        restMedicalHistoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMedicalHistory.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedMedicalHistory))
            )
            .andExpect(status().isOk());

        // Validate the MedicalHistory in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertMedicalHistoryUpdatableFieldsEquals(partialUpdatedMedicalHistory, getPersistedMedicalHistory(partialUpdatedMedicalHistory));
    }

    @Test
    @Transactional
    void patchNonExistingMedicalHistory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        medicalHistory.setId(longCount.incrementAndGet());

        // Create the MedicalHistory
        MedicalHistoryDTO medicalHistoryDTO = medicalHistoryMapper.toDto(medicalHistory);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMedicalHistoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, medicalHistoryDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(medicalHistoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MedicalHistory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchMedicalHistory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        medicalHistory.setId(longCount.incrementAndGet());

        // Create the MedicalHistory
        MedicalHistoryDTO medicalHistoryDTO = medicalHistoryMapper.toDto(medicalHistory);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMedicalHistoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(medicalHistoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MedicalHistory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamMedicalHistory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        medicalHistory.setId(longCount.incrementAndGet());

        // Create the MedicalHistory
        MedicalHistoryDTO medicalHistoryDTO = medicalHistoryMapper.toDto(medicalHistory);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMedicalHistoryMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(medicalHistoryDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the MedicalHistory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteMedicalHistory() throws Exception {
        // Initialize the database
        insertedMedicalHistory = medicalHistoryRepository.saveAndFlush(medicalHistory);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the medicalHistory
        restMedicalHistoryMockMvc
            .perform(delete(ENTITY_API_URL_ID, medicalHistory.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return medicalHistoryRepository.count();
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

    protected MedicalHistory getPersistedMedicalHistory(MedicalHistory medicalHistory) {
        return medicalHistoryRepository.findById(medicalHistory.getId()).orElseThrow();
    }

    protected void assertPersistedMedicalHistoryToMatchAllProperties(MedicalHistory expectedMedicalHistory) {
        assertMedicalHistoryAllPropertiesEquals(expectedMedicalHistory, getPersistedMedicalHistory(expectedMedicalHistory));
    }

    protected void assertPersistedMedicalHistoryToMatchUpdatableProperties(MedicalHistory expectedMedicalHistory) {
        assertMedicalHistoryAllUpdatablePropertiesEquals(expectedMedicalHistory, getPersistedMedicalHistory(expectedMedicalHistory));
    }
}
