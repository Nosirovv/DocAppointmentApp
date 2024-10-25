package uz.devops.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class DoctorTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Doctor getDoctorSample1() {
        return new Doctor().id(1L).name("name1");
    }

    public static Doctor getDoctorSample2() {
        return new Doctor().id(2L).name("name2");
    }

    public static Doctor getDoctorRandomSampleGenerator() {
        return new Doctor().id(longCount.incrementAndGet()).name(UUID.randomUUID().toString());
    }
}
