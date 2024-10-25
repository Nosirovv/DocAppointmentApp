package uz.devops.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class MedicalRecordTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static MedicalRecord getMedicalRecordSample1() {
        return new MedicalRecord().id(1L).sicknessInfo("sicknessInfo1").treatmentInto("treatmentInto1");
    }

    public static MedicalRecord getMedicalRecordSample2() {
        return new MedicalRecord().id(2L).sicknessInfo("sicknessInfo2").treatmentInto("treatmentInto2");
    }

    public static MedicalRecord getMedicalRecordRandomSampleGenerator() {
        return new MedicalRecord()
            .id(longCount.incrementAndGet())
            .sicknessInfo(UUID.randomUUID().toString())
            .treatmentInto(UUID.randomUUID().toString());
    }
}
