package uz.devops.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class MedicalHistoryTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static MedicalHistory getMedicalHistorySample1() {
        return new MedicalHistory().id(1L);
    }

    public static MedicalHistory getMedicalHistorySample2() {
        return new MedicalHistory().id(2L);
    }

    public static MedicalHistory getMedicalHistoryRandomSampleGenerator() {
        return new MedicalHistory().id(longCount.incrementAndGet());
    }
}
