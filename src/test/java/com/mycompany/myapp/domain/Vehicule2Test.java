package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.Parking2TestSamples.*;
import static com.mycompany.myapp.domain.Vehicule2TestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class Vehicule2Test {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Vehicule2.class);
        Vehicule2 vehicule21 = getVehicule2Sample1();
        Vehicule2 vehicule22 = new Vehicule2();
        assertThat(vehicule21).isNotEqualTo(vehicule22);

        vehicule22.setId(vehicule21.getId());
        assertThat(vehicule21).isEqualTo(vehicule22);

        vehicule22 = getVehicule2Sample2();
        assertThat(vehicule21).isNotEqualTo(vehicule22);
    }

    @Test
    void parking2Test() throws Exception {
        Vehicule2 vehicule2 = getVehicule2RandomSampleGenerator();
        Parking2 parking2Back = getParking2RandomSampleGenerator();

        vehicule2.setParking2(parking2Back);
        assertThat(vehicule2.getParking2()).isEqualTo(parking2Back);

        vehicule2.parking2(null);
        assertThat(vehicule2.getParking2()).isNull();
    }
}
