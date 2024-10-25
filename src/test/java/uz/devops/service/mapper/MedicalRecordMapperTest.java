package uz.devops.service.mapper;

import static uz.devops.domain.MedicalRecordAsserts.*;
import static uz.devops.domain.MedicalRecordTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MedicalRecordMapperTest {

    private MedicalRecordMapper medicalRecordMapper;

    @BeforeEach
    void setUp() {
        medicalRecordMapper = new MedicalRecordMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getMedicalRecordSample1();
        var actual = medicalRecordMapper.toEntity(medicalRecordMapper.toDto(expected));
        assertMedicalRecordAllPropertiesEquals(expected, actual);
    }
}
