package uz.devops.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import uz.devops.web.rest.TestUtil;

class MedicalHistoryDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(MedicalHistoryDTO.class);
        MedicalHistoryDTO medicalHistoryDTO1 = new MedicalHistoryDTO();
        medicalHistoryDTO1.setId(1L);
        MedicalHistoryDTO medicalHistoryDTO2 = new MedicalHistoryDTO();
        assertThat(medicalHistoryDTO1).isNotEqualTo(medicalHistoryDTO2);
        medicalHistoryDTO2.setId(medicalHistoryDTO1.getId());
        assertThat(medicalHistoryDTO1).isEqualTo(medicalHistoryDTO2);
        medicalHistoryDTO2.setId(2L);
        assertThat(medicalHistoryDTO1).isNotEqualTo(medicalHistoryDTO2);
        medicalHistoryDTO1.setId(null);
        assertThat(medicalHistoryDTO1).isNotEqualTo(medicalHistoryDTO2);
    }
}
