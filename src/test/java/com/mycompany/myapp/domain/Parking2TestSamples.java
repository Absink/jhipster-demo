package com.mycompany.myapp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class Parking2TestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Parking2 getParking2Sample1() {
        return new Parking2().id(1L).nom("nom1").nbPlaces(1);
    }

    public static Parking2 getParking2Sample2() {
        return new Parking2().id(2L).nom("nom2").nbPlaces(2);
    }

    public static Parking2 getParking2RandomSampleGenerator() {
        return new Parking2().id(longCount.incrementAndGet()).nom(UUID.randomUUID().toString()).nbPlaces(intCount.incrementAndGet());
    }
}
