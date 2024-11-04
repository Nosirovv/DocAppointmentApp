package uz.devops.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static uz.devops.domain.DoctorAsserts.*;
import static uz.devops.web.rest.TestUtil.createUpdateProxyForBean;
import static uz.devops.web.rest.TestUtil.sameInstant;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.time.*;
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
import uz.devops.domain.enumeration.Specialization;
import uz.devops.repository.DoctorRepository;
import uz.devops.service.dto.DoctorDTO;
import uz.devops.service.mapper.DoctorMapper;

/**
 * Integration tests for the {@link DoctorResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class DoctorResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Specialization DEFAULT_SPECIALIZATION = Specialization.GENERAL_PRACTITIONER;
    private static final Specialization UPDATED_SPECIALIZATION = Specialization.PEDIATRICIAN;

    // Vaqtning boshlang'ich (default) qiymatini 00:00 deb belgilaymiz
    private static final LocalTime DEFAULT_AVAILABLE_FROM = LocalTime.of(0, 0);
    private static final LocalTime UPDATED_AVAILABLE_FROM = LocalTime.now().withNano(0);
    private static final LocalTime SMALLER_AVAILABLE_FROM = LocalTime.of(23, 59); // Masalan, kechki 23:59

    // Tugash vaqtining boshlang'ich (default) qiymatini 00:00 deb belgilaymiz
    private static final LocalTime DEFAULT_AVAILABLE_TO = LocalTime.of(0, 0);
    private static final LocalTime UPDATED_AVAILABLE_TO = LocalTime.now().withNano(0);
    private static final LocalTime SMALLER_AVAILABLE_TO = LocalTime.of(23, 59); // Tugash vaqtiga kechki soat berish mumkin

    private static final String ENTITY_API_URL = "/api/doctors";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private DoctorMapper doctorMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDoctorMockMvc;

    private Doctor doctor;

    private Doctor insertedDoctor;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Doctor createEntity() {
        return new Doctor().name(DEFAULT_NAME).specialization(DEFAULT_SPECIALIZATION);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Doctor createUpdatedEntity() {
        return new Doctor().name(UPDATED_NAME).specialization(UPDATED_SPECIALIZATION);
    }

    @BeforeEach
    public void initTest() {
        doctor = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedDoctor != null) {
            doctorRepository.delete(insertedDoctor);
            insertedDoctor = null;
        }
    }

    @Test
    @Transactional
    void createDoctor() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Doctor
        DoctorDTO doctorDTO = doctorMapper.toDto(doctor);
        var returnedDoctorDTO = om.readValue(
            restDoctorMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(doctorDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            DoctorDTO.class
        );

        // Validate the Doctor in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedDoctor = doctorMapper.toEntity(returnedDoctorDTO);
        assertDoctorUpdatableFieldsEquals(returnedDoctor, getPersistedDoctor(returnedDoctor));

        insertedDoctor = returnedDoctor;
    }

    @Test
    @Transactional
    void createDoctorWithExistingId() throws Exception {
        // Create the Doctor with an existing ID
        doctor.setId(1L);
        DoctorDTO doctorDTO = doctorMapper.toDto(doctor);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDoctorMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(doctorDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Doctor in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        doctor.setName(null);

        // Create the Doctor, which fails.
        DoctorDTO doctorDTO = doctorMapper.toDto(doctor);

        restDoctorMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(doctorDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllDoctors() throws Exception {
        // Initialize the database
        insertedDoctor = doctorRepository.saveAndFlush(doctor);

        // Get all the doctorList
        restDoctorMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(doctor.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].specialization").value(hasItem(DEFAULT_SPECIALIZATION.toString())))
            .andExpect(jsonPath("$.[*].availableFrom").value(hasItem(DEFAULT_AVAILABLE_FROM.toString())))
            .andExpect(jsonPath("$.[*].availableTo").value(hasItem(DEFAULT_AVAILABLE_TO.toString())));
    }

    @Test
    @Transactional
    void getDoctor() throws Exception {
        // Initialize the database
        insertedDoctor = doctorRepository.saveAndFlush(doctor);

        // Get the doctor
        restDoctorMockMvc
            .perform(get(ENTITY_API_URL_ID, doctor.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(doctor.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.specialization").value(DEFAULT_SPECIALIZATION.toString()))
            .andExpect(jsonPath("$.[*].availableFrom").value(hasItem(DEFAULT_AVAILABLE_FROM.toString())))
            .andExpect(jsonPath("$.[*].availableTo").value(hasItem(DEFAULT_AVAILABLE_TO.toString())));
    }

    @Test
    @Transactional
    void getDoctorsByIdFiltering() throws Exception {
        // Initialize the database
        insertedDoctor = doctorRepository.saveAndFlush(doctor);

        Long id = doctor.getId();

        defaultDoctorFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultDoctorFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultDoctorFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllDoctorsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDoctor = doctorRepository.saveAndFlush(doctor);

        // Get all the doctorList where name equals to
        defaultDoctorFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllDoctorsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDoctor = doctorRepository.saveAndFlush(doctor);

        // Get all the doctorList where name in
        defaultDoctorFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllDoctorsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDoctor = doctorRepository.saveAndFlush(doctor);

        // Get all the doctorList where name is not null
        defaultDoctorFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    @Transactional
    void getAllDoctorsByNameContainsSomething() throws Exception {
        // Initialize the database
        insertedDoctor = doctorRepository.saveAndFlush(doctor);

        // Get all the doctorList where name contains
        defaultDoctorFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllDoctorsByNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedDoctor = doctorRepository.saveAndFlush(doctor);

        // Get all the doctorList where name does not contain
        defaultDoctorFiltering("name.doesNotContain=" + UPDATED_NAME, "name.doesNotContain=" + DEFAULT_NAME);
    }

    @Test
    @Transactional
    void getAllDoctorsBySpecializationIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDoctor = doctorRepository.saveAndFlush(doctor);

        // Get all the doctorList where specialization equals to
        defaultDoctorFiltering("specialization.equals=" + DEFAULT_SPECIALIZATION, "specialization.equals=" + UPDATED_SPECIALIZATION);
    }

    @Test
    @Transactional
    void getAllDoctorsBySpecializationIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDoctor = doctorRepository.saveAndFlush(doctor);

        // Get all the doctorList where specialization in
        defaultDoctorFiltering(
            "specialization.in=" + DEFAULT_SPECIALIZATION + "," + UPDATED_SPECIALIZATION,
            "specialization.in=" + UPDATED_SPECIALIZATION
        );
    }

    @Test
    @Transactional
    void getAllDoctorsBySpecializationIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDoctor = doctorRepository.saveAndFlush(doctor);

        // Get all the doctorList where specialization is not null
        defaultDoctorFiltering("specialization.specified=true", "specialization.specified=false");
    }

    @Test
    @Transactional
    void getAllDoctorsByAvailableFromIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDoctor = doctorRepository.saveAndFlush(doctor);

        // Get all the doctorList where availableFrom equals to
        defaultDoctorFiltering("availableFrom.equals=" + DEFAULT_AVAILABLE_FROM, "availableFrom.equals=" + UPDATED_AVAILABLE_FROM);
    }

    @Test
    @Transactional
    void getAllDoctorsByAvailableFromIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDoctor = doctorRepository.saveAndFlush(doctor);

        // Get all the doctorList where availableFrom in
        defaultDoctorFiltering(
            "availableFrom.in=" + DEFAULT_AVAILABLE_FROM + "," + UPDATED_AVAILABLE_FROM,
            "availableFrom.in=" + UPDATED_AVAILABLE_FROM
        );
    }

    @Test
    @Transactional
    void getAllDoctorsByAvailableFromIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDoctor = doctorRepository.saveAndFlush(doctor);

        // Get all the doctorList where availableFrom is not null
        defaultDoctorFiltering("availableFrom.specified=true", "availableFrom.specified=false");
    }

    @Test
    @Transactional
    void getAllDoctorsByAvailableFromIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedDoctor = doctorRepository.saveAndFlush(doctor);

        // Get all the doctorList where availableFrom is greater than or equal to
        defaultDoctorFiltering(
            "availableFrom.greaterThanOrEqual=" + DEFAULT_AVAILABLE_FROM,
            "availableFrom.greaterThanOrEqual=" + UPDATED_AVAILABLE_FROM
        );
    }

    @Test
    @Transactional
    void getAllDoctorsByAvailableFromIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedDoctor = doctorRepository.saveAndFlush(doctor);

        // Get all the doctorList where availableFrom is less than or equal to
        defaultDoctorFiltering(
            "availableFrom.lessThanOrEqual=" + DEFAULT_AVAILABLE_FROM,
            "availableFrom.lessThanOrEqual=" + SMALLER_AVAILABLE_FROM
        );
    }

    @Test
    @Transactional
    void getAllDoctorsByAvailableFromIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedDoctor = doctorRepository.saveAndFlush(doctor);

        // Get all the doctorList where availableFrom is less than
        defaultDoctorFiltering("availableFrom.lessThan=" + UPDATED_AVAILABLE_FROM, "availableFrom.lessThan=" + DEFAULT_AVAILABLE_FROM);
    }

    @Test
    @Transactional
    void getAllDoctorsByAvailableFromIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedDoctor = doctorRepository.saveAndFlush(doctor);

        // Get all the doctorList where availableFrom is greater than
        defaultDoctorFiltering(
            "availableFrom.greaterThan=" + SMALLER_AVAILABLE_FROM,
            "availableFrom.greaterThan=" + DEFAULT_AVAILABLE_FROM
        );
    }

    @Test
    @Transactional
    void getAllDoctorsByAvailableToIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDoctor = doctorRepository.saveAndFlush(doctor);

        // Get all the doctorList where availableTo equals to
        defaultDoctorFiltering("availableTo.equals=" + DEFAULT_AVAILABLE_TO, "availableTo.equals=" + UPDATED_AVAILABLE_TO);
    }

    @Test
    @Transactional
    void getAllDoctorsByAvailableToIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDoctor = doctorRepository.saveAndFlush(doctor);

        // Get all the doctorList where availableTo in
        defaultDoctorFiltering(
            "availableTo.in=" + DEFAULT_AVAILABLE_TO + "," + UPDATED_AVAILABLE_TO,
            "availableTo.in=" + UPDATED_AVAILABLE_TO
        );
    }

    @Test
    @Transactional
    void getAllDoctorsByAvailableToIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDoctor = doctorRepository.saveAndFlush(doctor);

        // Get all the doctorList where availableTo is not null
        defaultDoctorFiltering("availableTo.specified=true", "availableTo.specified=false");
    }

    @Test
    @Transactional
    void getAllDoctorsByAvailableToIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedDoctor = doctorRepository.saveAndFlush(doctor);

        // Get all the doctorList where availableTo is greater than or equal to
        defaultDoctorFiltering(
            "availableTo.greaterThanOrEqual=" + DEFAULT_AVAILABLE_TO,
            "availableTo.greaterThanOrEqual=" + UPDATED_AVAILABLE_TO
        );
    }

    @Test
    @Transactional
    void getAllDoctorsByAvailableToIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedDoctor = doctorRepository.saveAndFlush(doctor);

        // Get all the doctorList where availableTo is less than or equal to
        defaultDoctorFiltering(
            "availableTo.lessThanOrEqual=" + DEFAULT_AVAILABLE_TO,
            "availableTo.lessThanOrEqual=" + SMALLER_AVAILABLE_TO
        );
    }

    @Test
    @Transactional
    void getAllDoctorsByAvailableToIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedDoctor = doctorRepository.saveAndFlush(doctor);

        // Get all the doctorList where availableTo is less than
        defaultDoctorFiltering("availableTo.lessThan=" + UPDATED_AVAILABLE_TO, "availableTo.lessThan=" + DEFAULT_AVAILABLE_TO);
    }

    @Test
    @Transactional
    void getAllDoctorsByAvailableToIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedDoctor = doctorRepository.saveAndFlush(doctor);

        // Get all the doctorList where availableTo is greater than
        defaultDoctorFiltering("availableTo.greaterThan=" + SMALLER_AVAILABLE_TO, "availableTo.greaterThan=" + DEFAULT_AVAILABLE_TO);
    }

    private void defaultDoctorFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultDoctorShouldBeFound(shouldBeFound);
        defaultDoctorShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultDoctorShouldBeFound(String filter) throws Exception {
        restDoctorMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(doctor.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].specialization").value(hasItem(DEFAULT_SPECIALIZATION.toString())))
            .andExpect(jsonPath("$.[*].availableFrom").value(hasItem(DEFAULT_AVAILABLE_FROM.toString())))
            .andExpect(jsonPath("$.[*].availableTo").value(hasItem(DEFAULT_AVAILABLE_TO.toString())));

        // Check, that the count call also returns 1
        restDoctorMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultDoctorShouldNotBeFound(String filter) throws Exception {
        restDoctorMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restDoctorMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingDoctor() throws Exception {
        // Get the doctor
        restDoctorMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingDoctor() throws Exception {
        // Initialize the database
        insertedDoctor = doctorRepository.saveAndFlush(doctor);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the doctor
        Doctor updatedDoctor = doctorRepository.findById(doctor.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedDoctor are not directly saved in db
        em.detach(updatedDoctor);
        updatedDoctor.name(UPDATED_NAME).specialization(UPDATED_SPECIALIZATION);
        DoctorDTO doctorDTO = doctorMapper.toDto(updatedDoctor);

        restDoctorMockMvc
            .perform(
                put(ENTITY_API_URL_ID, doctorDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(doctorDTO))
            )
            .andExpect(status().isOk());

        // Validate the Doctor in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedDoctorToMatchAllProperties(updatedDoctor);
    }

    @Test
    @Transactional
    void putNonExistingDoctor() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        doctor.setId(longCount.incrementAndGet());

        // Create the Doctor
        DoctorDTO doctorDTO = doctorMapper.toDto(doctor);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDoctorMockMvc
            .perform(
                put(ENTITY_API_URL_ID, doctorDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(doctorDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Doctor in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDoctor() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        doctor.setId(longCount.incrementAndGet());

        // Create the Doctor
        DoctorDTO doctorDTO = doctorMapper.toDto(doctor);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDoctorMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(doctorDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Doctor in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDoctor() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        doctor.setId(longCount.incrementAndGet());

        // Create the Doctor
        DoctorDTO doctorDTO = doctorMapper.toDto(doctor);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDoctorMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(doctorDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Doctor in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDoctorWithPatch() throws Exception {
        // Initialize the database
        insertedDoctor = doctorRepository.saveAndFlush(doctor);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the doctor using partial update
        Doctor partialUpdatedDoctor = new Doctor();
        partialUpdatedDoctor.setId(doctor.getId());

        partialUpdatedDoctor.specialization(UPDATED_SPECIALIZATION);

        restDoctorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDoctor.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedDoctor))
            )
            .andExpect(status().isOk());

        // Validate the Doctor in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertDoctorUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedDoctor, doctor), getPersistedDoctor(doctor));
    }

    @Test
    @Transactional
    void fullUpdateDoctorWithPatch() throws Exception {
        // Initialize the database
        insertedDoctor = doctorRepository.saveAndFlush(doctor);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the doctor using partial update
        Doctor partialUpdatedDoctor = new Doctor();
        partialUpdatedDoctor.setId(doctor.getId());

        partialUpdatedDoctor.name(UPDATED_NAME).specialization(UPDATED_SPECIALIZATION);

        restDoctorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDoctor.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedDoctor))
            )
            .andExpect(status().isOk());

        // Validate the Doctor in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertDoctorUpdatableFieldsEquals(partialUpdatedDoctor, getPersistedDoctor(partialUpdatedDoctor));
    }

    @Test
    @Transactional
    void patchNonExistingDoctor() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        doctor.setId(longCount.incrementAndGet());

        // Create the Doctor
        DoctorDTO doctorDTO = doctorMapper.toDto(doctor);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDoctorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, doctorDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(doctorDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Doctor in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDoctor() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        doctor.setId(longCount.incrementAndGet());

        // Create the Doctor
        DoctorDTO doctorDTO = doctorMapper.toDto(doctor);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDoctorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(doctorDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Doctor in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDoctor() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        doctor.setId(longCount.incrementAndGet());

        // Create the Doctor
        DoctorDTO doctorDTO = doctorMapper.toDto(doctor);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDoctorMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(doctorDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Doctor in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDoctor() throws Exception {
        // Initialize the database
        insertedDoctor = doctorRepository.saveAndFlush(doctor);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the doctor
        restDoctorMockMvc
            .perform(delete(ENTITY_API_URL_ID, doctor.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return doctorRepository.count();
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

    protected Doctor getPersistedDoctor(Doctor doctor) {
        return doctorRepository.findById(doctor.getId()).orElseThrow();
    }

    protected void assertPersistedDoctorToMatchAllProperties(Doctor expectedDoctor) {
        assertDoctorAllPropertiesEquals(expectedDoctor, getPersistedDoctor(expectedDoctor));
    }

    protected void assertPersistedDoctorToMatchUpdatableProperties(Doctor expectedDoctor) {
        assertDoctorAllUpdatablePropertiesEquals(expectedDoctor, getPersistedDoctor(expectedDoctor));
    }
}
