package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.Parking2TestSamples.*;
import static com.mycompany.myapp.domain.Vehicule2TestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class Parking2Test {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Parking2.class);
        Parking2 parking21 = getParking2Sample1();
        Parking2 parking22 = new Parking2();
        assertThat(parking21).isNotEqualTo(parking22);

        parking22.setId(parking21.getId());
        assertThat(parking21).isEqualTo(parking22);

        parking22 = getParking2Sample2();
        assertThat(parking21).isNotEqualTo(parking22);
    }

    @Test
    void vehicule2Test() throws Exception {
        Parking2 parking2 = getParking2RandomSampleGenerator();
        Vehicule2 vehicule2Back = getVehicule2RandomSampleGenerator();

        parking2.addVehicule2(vehicule2Back);
        assertThat(parking2.getVehicule2s()).containsOnly(vehicule2Back);
        assertThat(vehicule2Back.getParking2()).isEqualTo(parking2);

        parking2.removeVehicule2(vehicule2Back);
        assertThat(parking2.getVehicule2s()).doesNotContain(vehicule2Back);
        assertThat(vehicule2Back.getParking2()).isNull();

        parking2.vehicule2s(new HashSet<>(Set.of(vehicule2Back)));
        assertThat(parking2.getVehicule2s()).containsOnly(vehicule2Back);
        assertThat(vehicule2Back.getParking2()).isEqualTo(parking2);

        parking2.setVehicule2s(new HashSet<>());
        assertThat(parking2.getVehicule2s()).doesNotContain(vehicule2Back);
        assertThat(vehicule2Back.getParking2()).isNull();
    }
}
