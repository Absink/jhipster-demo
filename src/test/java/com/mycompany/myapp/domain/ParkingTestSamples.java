package com.mycompany.myapp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class ParkingTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Parking getParkingSample1() {
        return new Parking().id(1L).nom("nom1").nb_places(1);
    }

    public static Parking getParkingSample2() {
        return new Parking().id(2L).nom("nom2").nb_places(2);
    }

    public static Parking getParkingRandomSampleGenerator() {
        return new Parking().id(longCount.incrementAndGet()).nom(UUID.randomUUID().toString()).nb_places(intCount.incrementAndGet());
    }
}
