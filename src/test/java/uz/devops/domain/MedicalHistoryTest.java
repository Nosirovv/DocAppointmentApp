package uz.devops.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static uz.devops.domain.MedicalHistoryTestSamples.*;
import static uz.devops.domain.MedicalRecordTestSamples.*;

import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;
import uz.devops.web.rest.TestUtil;

class MedicalHistoryTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(MedicalHistory.class);
        MedicalHistory medicalHistory1 = getMedicalHistorySample1();
        MedicalHistory medicalHistory2 = new MedicalHistory();
        assertThat(medicalHistory1).isNotEqualTo(medicalHistory2);

        medicalHistory2.setId(medicalHistory1.getId());
        assertThat(medicalHistory1).isEqualTo(medicalHistory2);

        medicalHistory2 = getMedicalHistorySample2();
        assertThat(medicalHistory1).isNotEqualTo(medicalHistory2);
    }

    @Test
    void recordsTest() {
        MedicalHistory medicalHistory = getMedicalHistoryRandomSampleGenerator();
        MedicalRecord medicalRecordBack = getMedicalRecordRandomSampleGenerator();

        medicalHistory.addRecords(medicalRecordBack);
        assertThat(medicalHistory.getRecords()).containsOnly(medicalRecordBack);
        assertThat(medicalRecordBack.getHistory()).isEqualTo(medicalHistory);

        medicalHistory.removeRecords(medicalRecordBack);
        assertThat(medicalHistory.getRecords()).doesNotContain(medicalRecordBack);
        assertThat(medicalRecordBack.getHistory()).isNull();

        medicalHistory.records(new HashSet<>(Set.of(medicalRecordBack)));
        assertThat(medicalHistory.getRecords()).containsOnly(medicalRecordBack);
        assertThat(medicalRecordBack.getHistory()).isEqualTo(medicalHistory);

        medicalHistory.setRecords(new HashSet<>());
        assertThat(medicalHistory.getRecords()).doesNotContain(medicalRecordBack);
        assertThat(medicalRecordBack.getHistory()).isNull();
    }
}
