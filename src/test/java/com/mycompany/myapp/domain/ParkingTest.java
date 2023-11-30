package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.ParkingTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ParkingTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Parking.class);
        Parking parking1 = getParkingSample1();
        Parking parking2 = new Parking();
        assertThat(parking1).isNotEqualTo(parking2);

        parking2.setId(parking1.getId());
        assertThat(parking1).isEqualTo(parking2);

        parking2 = getParkingSample2();
        assertThat(parking1).isNotEqualTo(parking2);
    }
}
