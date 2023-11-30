package com.mycompany.myapp.web.rest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.mycompany.myapp.IntegrationTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

/**
 * Test class for the StatistiquesResource REST controller.
 *
 * @see StatistiquesResource
 */
@IntegrationTest
class StatistiquesResourceIT {

    private MockMvc restMockMvc;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        StatistiquesResource statistiquesResource = new StatistiquesResource(null);
        restMockMvc = MockMvcBuilders.standaloneSetup(statistiquesResource).build();
    }

    /**
     * Test vehiculesByMarque
     */
    @Test
    void testVehiculesByMarque() throws Exception {
        restMockMvc.perform(get("/api/statistiques/vehicules-by-marque")).andExpect(status().isOk());
    }

    /**
     * Test updateParkingPlaces
     */
    @Test
    void testUpdateParkingPlaces() throws Exception {
        restMockMvc.perform(put("/api/statistiques/update-parking-places")).andExpect(status().isOk());
    }
}
