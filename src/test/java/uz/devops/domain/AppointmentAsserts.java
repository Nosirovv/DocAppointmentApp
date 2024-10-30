package uz.devops.domain;

import static org.assertj.core.api.Assertions.assertThat;

public class AppointmentAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertAppointmentAllPropertiesEquals(Appointment expected, Appointment actual) {
        assertAppointmentAutoGeneratedPropertiesEquals(expected, actual);
        assertAppointmentAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertAppointmentAllUpdatablePropertiesEquals(Appointment expected, Appointment actual) {
        assertAppointmentUpdatableFieldsEquals(expected, actual);
        assertAppointmentUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertAppointmentAutoGeneratedPropertiesEquals(Appointment expected, Appointment actual) {
        assertThat(expected)
            .as("Verify Appointment auto generated properties")
            .satisfies(e -> assertThat(e.getId()).as("check id").isEqualTo(actual.getId()));
    }

    /**
     * Asserts that the entity has all the updatable fields set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertAppointmentUpdatableFieldsEquals(Appointment expected, Appointment actual) {
        assertThat(expected)
            .as("Verify Appointment relevant properties")
            .satisfies(e ->
                assertThat(e.getAppointmentStartTime()).as("check appointmentDateTime").isEqualTo(actual.getAppointmentStartTime())
            )
            .satisfies(e -> assertThat(e.getStatus()).as("check status").isEqualTo(actual.getStatus()));
    }

    /**
     * Asserts that the entity has all the updatable relationships set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertAppointmentUpdatableRelationshipsEquals(Appointment expected, Appointment actual) {
        assertThat(expected)
            .as("Verify Appointment relationships")
            .satisfies(e -> assertThat(e.getDoctor()).as("check doctor").isEqualTo(actual.getDoctor()))
            .satisfies(e -> assertThat(e.getPatient()).as("check patient").isEqualTo(actual.getPatient()));
    }
}
