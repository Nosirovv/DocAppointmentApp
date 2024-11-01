package uz.devops.domain;

import static org.assertj.core.api.Assertions.assertThat;

public class DoctorAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertDoctorAllPropertiesEquals(Doctor expected, Doctor actual) {
        assertDoctorAutoGeneratedPropertiesEquals(expected, actual);
        assertDoctorAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertDoctorAllUpdatablePropertiesEquals(Doctor expected, Doctor actual) {
        assertDoctorUpdatableFieldsEquals(expected, actual);
        assertDoctorUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertDoctorAutoGeneratedPropertiesEquals(Doctor expected, Doctor actual) {
        assertThat(expected)
            .as("Verify Doctor auto generated properties")
            .satisfies(e -> assertThat(e.getId()).as("check id").isEqualTo(actual.getId()));
    }

    /**
     * Asserts that the entity has all the updatable fields set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertDoctorUpdatableFieldsEquals(Doctor expected, Doctor actual) {
        assertThat(expected)
            .as("Verify Doctor relevant properties")
            .satisfies(e -> assertThat(e.getName()).as("check name").isEqualTo(actual.getName()))
            .satisfies(e -> assertThat(e.getSpecialization()).as("check specialization").isEqualTo(actual.getSpecialization()))
            .satisfies(e -> assertThat(e.getAvailableFrom()).as("check availableFrom").isEqualTo(actual.getAvailableFrom()))
            .satisfies(e -> assertThat(e.getAvailableTo()).as("check availableTo").isEqualTo(actual.getAvailableTo()));
    }

    /**
     * Asserts that the entity has all the updatable relationships set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertDoctorUpdatableRelationshipsEquals(Doctor expected, Doctor actual) {
        // empty method
    }
}
