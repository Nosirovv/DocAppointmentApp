package uz.devops.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import uz.devops.web.rest.TestUtil;

class DoctorDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(DoctorDTO.class);
        DoctorDTO doctorDTO1 = new DoctorDTO();
        doctorDTO1.setId(1L);
        DoctorDTO doctorDTO2 = new DoctorDTO();
        assertThat(doctorDTO1).isNotEqualTo(doctorDTO2);
        doctorDTO2.setId(doctorDTO1.getId());
        assertThat(doctorDTO1).isEqualTo(doctorDTO2);
        doctorDTO2.setId(2L);
        assertThat(doctorDTO1).isNotEqualTo(doctorDTO2);
        doctorDTO1.setId(null);
        assertThat(doctorDTO1).isNotEqualTo(doctorDTO2);
    }
}
