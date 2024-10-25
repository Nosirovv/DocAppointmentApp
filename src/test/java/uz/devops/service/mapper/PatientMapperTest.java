package uz.devops.service.mapper;

import static uz.devops.domain.PatientAsserts.*;
import static uz.devops.domain.PatientTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PatientMapperTest {

    private PatientMapper patientMapper;

    @BeforeEach
    void setUp() {
        patientMapper = new PatientMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getPatientSample1();
        var actual = patientMapper.toEntity(patientMapper.toDto(expected));
        assertPatientAllPropertiesEquals(expected, actual);
    }
}
