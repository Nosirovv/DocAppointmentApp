package uz.devops.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static uz.devops.domain.DoctorWorkScheduleAsserts.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
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
import uz.devops.domain.DoctorWorkSchedule;
import uz.devops.repository.DoctorWorkScheduleRepository;

/**
 * Integration tests for the {@link DoctorWorkScheduleResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class DoctorWorkScheduleResourceIT {

    private static final String ENTITY_API_URL = "/api/doctor-work-schedules";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private ObjectMapper om;

    @Autowired
    private DoctorWorkScheduleRepository doctorWorkScheduleRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDoctorWorkScheduleMockMvc;

    private DoctorWorkSchedule doctorWorkSchedule;

    private DoctorWorkSchedule insertedDoctorWorkSchedule;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DoctorWorkSchedule createEntity() {
        return new DoctorWorkSchedule();
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DoctorWorkSchedule createUpdatedEntity() {
        return new DoctorWorkSchedule();
    }

    @BeforeEach
    public void initTest() {
        doctorWorkSchedule = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedDoctorWorkSchedule != null) {
            doctorWorkScheduleRepository.delete(insertedDoctorWorkSchedule);
            insertedDoctorWorkSchedule = null;
        }
    }

    @Test
    @Transactional
    void createDoctorWorkSchedule() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the DoctorWorkSchedule
        var returnedDoctorWorkSchedule = om.readValue(
            restDoctorWorkScheduleMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(doctorWorkSchedule)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            DoctorWorkSchedule.class
        );

        // Validate the DoctorWorkSchedule in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertDoctorWorkScheduleUpdatableFieldsEquals(
            returnedDoctorWorkSchedule,
            getPersistedDoctorWorkSchedule(returnedDoctorWorkSchedule)
        );

        insertedDoctorWorkSchedule = returnedDoctorWorkSchedule;
    }

    @Test
    @Transactional
    void createDoctorWorkScheduleWithExistingId() throws Exception {
        // Create the DoctorWorkSchedule with an existing ID
        doctorWorkSchedule.setId(1);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDoctorWorkScheduleMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(doctorWorkSchedule)))
            .andExpect(status().isBadRequest());

        // Validate the DoctorWorkSchedule in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllDoctorWorkSchedules() throws Exception {
        // Initialize the database
        insertedDoctorWorkSchedule = doctorWorkScheduleRepository.saveAndFlush(doctorWorkSchedule);

        // Get all the doctorWorkScheduleList
        restDoctorWorkScheduleMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(doctorWorkSchedule.getId().intValue())));
    }

    @Test
    @Transactional
    void getDoctorWorkSchedule() throws Exception {
        // Initialize the database
        insertedDoctorWorkSchedule = doctorWorkScheduleRepository.saveAndFlush(doctorWorkSchedule);

        // Get the doctorWorkSchedule
        restDoctorWorkScheduleMockMvc
            .perform(get(ENTITY_API_URL_ID, doctorWorkSchedule.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(doctorWorkSchedule.getId().intValue()));
    }

    @Test
    @Transactional
    void getNonExistingDoctorWorkSchedule() throws Exception {
        // Get the doctorWorkSchedule
        restDoctorWorkScheduleMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void deleteDoctorWorkSchedule() throws Exception {
        // Initialize the database
        insertedDoctorWorkSchedule = doctorWorkScheduleRepository.saveAndFlush(doctorWorkSchedule);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the doctorWorkSchedule
        restDoctorWorkScheduleMockMvc
            .perform(delete(ENTITY_API_URL_ID, doctorWorkSchedule.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return doctorWorkScheduleRepository.count();
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

    protected DoctorWorkSchedule getPersistedDoctorWorkSchedule(DoctorWorkSchedule doctorWorkSchedule) {
        return doctorWorkScheduleRepository.findById(doctorWorkSchedule.getDoctorId()).orElseThrow();
    }

    protected void assertPersistedDoctorWorkScheduleToMatchAllProperties(DoctorWorkSchedule expectedDoctorWorkSchedule) {
        assertDoctorWorkScheduleAllPropertiesEquals(expectedDoctorWorkSchedule, getPersistedDoctorWorkSchedule(expectedDoctorWorkSchedule));
    }

    protected void assertPersistedDoctorWorkScheduleToMatchUpdatableProperties(DoctorWorkSchedule expectedDoctorWorkSchedule) {
        assertDoctorWorkScheduleAllUpdatablePropertiesEquals(
            expectedDoctorWorkSchedule,
            getPersistedDoctorWorkSchedule(expectedDoctorWorkSchedule)
        );
    }
}
