package uz.devops.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static uz.devops.domain.AppointmentTestSamples.*;
import static uz.devops.domain.PatientTestSamples.*;

import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;
import uz.devops.web.rest.TestUtil;

class PatientTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Patient.class);
        Patient patient1 = getPatientSample1();
        Patient patient2 = new Patient();
        assertThat(patient1).isNotEqualTo(patient2);

        patient2.setId(patient1.getId());
        assertThat(patient1).isEqualTo(patient2);

        patient2 = getPatientSample2();
        assertThat(patient1).isNotEqualTo(patient2);
    }

    @Test
    void appointmentsTest() {
        Patient patient = getPatientRandomSampleGenerator();
        Appointment appointmentBack = getAppointmentRandomSampleGenerator();

        patient.addAppointments(appointmentBack);
        assertThat(patient.getAppointments()).containsOnly(appointmentBack);
        assertThat(appointmentBack.getPatient()).isEqualTo(patient);

        patient.removeAppointments(appointmentBack);
        assertThat(patient.getAppointments()).doesNotContain(appointmentBack);
        assertThat(appointmentBack.getPatient()).isNull();

        patient.appointments(new HashSet<>(Set.of(appointmentBack)));
        assertThat(patient.getAppointments()).containsOnly(appointmentBack);
        assertThat(appointmentBack.getPatient()).isEqualTo(patient);

        patient.setAppointments(new HashSet<>());
        assertThat(patient.getAppointments()).doesNotContain(appointmentBack);
        assertThat(appointmentBack.getPatient()).isNull();
    }
}
