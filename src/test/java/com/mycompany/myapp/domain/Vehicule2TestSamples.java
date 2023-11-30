package com.mycompany.myapp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class Vehicule2TestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Vehicule2 getVehicule2Sample1() {
        return new Vehicule2().id(1L).nom("nom1").prix(1).nbChevaux(1);
    }

    public static Vehicule2 getVehicule2Sample2() {
        return new Vehicule2().id(2L).nom("nom2").prix(2).nbChevaux(2);
    }

    public static Vehicule2 getVehicule2RandomSampleGenerator() {
        return new Vehicule2()
            .id(longCount.incrementAndGet())
            .nom(UUID.randomUUID().toString())
            .prix(intCount.incrementAndGet())
            .nbChevaux(intCount.incrementAndGet());
    }
}
