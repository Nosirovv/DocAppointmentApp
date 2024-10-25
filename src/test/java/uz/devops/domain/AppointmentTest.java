package uz.devops.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static uz.devops.domain.AppointmentTestSamples.*;
import static uz.devops.domain.DoctorTestSamples.*;
import static uz.devops.domain.PatientTestSamples.*;

import org.junit.jupiter.api.Test;
import uz.devops.web.rest.TestUtil;

class AppointmentTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Appointment.class);
        Appointment appointment1 = getAppointmentSample1();
        Appointment appointment2 = new Appointment();
        assertThat(appointment1).isNotEqualTo(appointment2);

        appointment2.setId(appointment1.getId());
        assertThat(appointment1).isEqualTo(appointment2);

        appointment2 = getAppointmentSample2();
        assertThat(appointment1).isNotEqualTo(appointment2);
    }

    @Test
    void doctorTest() {
        Appointment appointment = getAppointmentRandomSampleGenerator();
        Doctor doctorBack = getDoctorRandomSampleGenerator();

        appointment.setDoctor(doctorBack);
        assertThat(appointment.getDoctor()).isEqualTo(doctorBack);

        appointment.doctor(null);
        assertThat(appointment.getDoctor()).isNull();
    }

    @Test
    void patientTest() {
        Appointment appointment = getAppointmentRandomSampleGenerator();
        Patient patientBack = getPatientRandomSampleGenerator();

        appointment.setPatient(patientBack);
        assertThat(appointment.getPatient()).isEqualTo(patientBack);

        appointment.patient(null);
        assertThat(appointment.getPatient()).isNull();
    }
}
