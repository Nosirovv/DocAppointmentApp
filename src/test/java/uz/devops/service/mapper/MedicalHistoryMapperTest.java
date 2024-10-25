package uz.devops.service.mapper;

import static uz.devops.domain.MedicalHistoryAsserts.*;
import static uz.devops.domain.MedicalHistoryTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MedicalHistoryMapperTest {

    private MedicalHistoryMapper medicalHistoryMapper;

    @BeforeEach
    void setUp() {
        medicalHistoryMapper = new MedicalHistoryMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getMedicalHistorySample1();
        var actual = medicalHistoryMapper.toEntity(medicalHistoryMapper.toDto(expected));
        assertMedicalHistoryAllPropertiesEquals(expected, actual);
    }
}
