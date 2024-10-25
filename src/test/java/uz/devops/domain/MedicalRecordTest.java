package uz.devops.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static uz.devops.domain.DoctorTestSamples.*;
import static uz.devops.domain.MedicalHistoryTestSamples.*;
import static uz.devops.domain.MedicalRecordTestSamples.*;

import org.junit.jupiter.api.Test;
import uz.devops.web.rest.TestUtil;

class MedicalRecordTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(MedicalRecord.class);
        MedicalRecord medicalRecord1 = getMedicalRecordSample1();
        MedicalRecord medicalRecord2 = new MedicalRecord();
        assertThat(medicalRecord1).isNotEqualTo(medicalRecord2);

        medicalRecord2.setId(medicalRecord1.getId());
        assertThat(medicalRecord1).isEqualTo(medicalRecord2);

        medicalRecord2 = getMedicalRecordSample2();
        assertThat(medicalRecord1).isNotEqualTo(medicalRecord2);
    }

    @Test
    void doctorTest() {
        MedicalRecord medicalRecord = getMedicalRecordRandomSampleGenerator();
        Doctor doctorBack = getDoctorRandomSampleGenerator();

        medicalRecord.setDoctor(doctorBack);
        assertThat(medicalRecord.getDoctor()).isEqualTo(doctorBack);

        medicalRecord.doctor(null);
        assertThat(medicalRecord.getDoctor()).isNull();
    }

    @Test
    void historyTest() {
        MedicalRecord medicalRecord = getMedicalRecordRandomSampleGenerator();
        MedicalHistory medicalHistoryBack = getMedicalHistoryRandomSampleGenerator();

        medicalRecord.setHistory(medicalHistoryBack);
        assertThat(medicalRecord.getHistory()).isEqualTo(medicalHistoryBack);

        medicalRecord.history(null);
        assertThat(medicalRecord.getHistory()).isNull();
    }
}
