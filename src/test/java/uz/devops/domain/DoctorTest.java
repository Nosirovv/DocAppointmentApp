package uz.devops.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static uz.devops.domain.AppointmentTestSamples.*;
import static uz.devops.domain.DoctorTestSamples.*;
import static uz.devops.domain.MedicalRecordTestSamples.*;

import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;
import uz.devops.web.rest.TestUtil;

class DoctorTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Doctor.class);
        Doctor doctor1 = getDoctorSample1();
        Doctor doctor2 = new Doctor();
        assertThat(doctor1).isNotEqualTo(doctor2);

        doctor2.setId(doctor1.getId());
        assertThat(doctor1).isEqualTo(doctor2);

        doctor2 = getDoctorSample2();
        assertThat(doctor1).isNotEqualTo(doctor2);
    }

    @Test
    void appointmentsTest() {
        Doctor doctor = getDoctorRandomSampleGenerator();
        Appointment appointmentBack = getAppointmentRandomSampleGenerator();

        doctor.addAppointments(appointmentBack);
        assertThat(doctor.getAppointments()).containsOnly(appointmentBack);
        assertThat(appointmentBack.getDoctor()).isEqualTo(doctor);

        doctor.removeAppointments(appointmentBack);
        assertThat(doctor.getAppointments()).doesNotContain(appointmentBack);
        assertThat(appointmentBack.getDoctor()).isNull();

        doctor.appointments(new HashSet<>(Set.of(appointmentBack)));
        assertThat(doctor.getAppointments()).containsOnly(appointmentBack);
        assertThat(appointmentBack.getDoctor()).isEqualTo(doctor);

        doctor.setAppointments(new HashSet<>());
        assertThat(doctor.getAppointments()).doesNotContain(appointmentBack);
        assertThat(appointmentBack.getDoctor()).isNull();
    }

    @Test
    void recordsTest() {
        Doctor doctor = getDoctorRandomSampleGenerator();
        MedicalRecord medicalRecordBack = getMedicalRecordRandomSampleGenerator();

        doctor.addRecords(medicalRecordBack);
        assertThat(doctor.getRecords()).containsOnly(medicalRecordBack);
        assertThat(medicalRecordBack.getDoctor()).isEqualTo(doctor);

        doctor.removeRecords(medicalRecordBack);
        assertThat(doctor.getRecords()).doesNotContain(medicalRecordBack);
        assertThat(medicalRecordBack.getDoctor()).isNull();

        doctor.records(new HashSet<>(Set.of(medicalRecordBack)));
        assertThat(doctor.getRecords()).containsOnly(medicalRecordBack);
        assertThat(medicalRecordBack.getDoctor()).isEqualTo(doctor);

        doctor.setRecords(new HashSet<>());
        assertThat(doctor.getRecords()).doesNotContain(medicalRecordBack);
        assertThat(medicalRecordBack.getDoctor()).isNull();
    }
}
