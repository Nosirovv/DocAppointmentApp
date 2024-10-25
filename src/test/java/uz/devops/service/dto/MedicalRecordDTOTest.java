package uz.devops.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import uz.devops.web.rest.TestUtil;

class MedicalRecordDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(MedicalRecordDTO.class);
        MedicalRecordDTO medicalRecordDTO1 = new MedicalRecordDTO();
        medicalRecordDTO1.setId(1L);
        MedicalRecordDTO medicalRecordDTO2 = new MedicalRecordDTO();
        assertThat(medicalRecordDTO1).isNotEqualTo(medicalRecordDTO2);
        medicalRecordDTO2.setId(medicalRecordDTO1.getId());
        assertThat(medicalRecordDTO1).isEqualTo(medicalRecordDTO2);
        medicalRecordDTO2.setId(2L);
        assertThat(medicalRecordDTO1).isNotEqualTo(medicalRecordDTO2);
        medicalRecordDTO1.setId(null);
        assertThat(medicalRecordDTO1).isNotEqualTo(medicalRecordDTO2);
    }
}
