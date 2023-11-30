package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Parking;
import com.mycompany.myapp.repository.ParkingRepository;
import jakarta.persistence.EntityManager;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link ParkingResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ParkingResourceIT {

    private static final String DEFAULT_NOM = "AAAAAAAAAA";
    private static final String UPDATED_NOM = "BBBBBBBBBB";

    private static final Integer DEFAULT_NB_PLACES = 1;
    private static final Integer UPDATED_NB_PLACES = 2;

    private static final LocalDate DEFAULT_DATE_CREATION = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_CREATION = LocalDate.now(ZoneId.systemDefault());

    private static final Boolean DEFAULT_IS_OPEN = false;
    private static final Boolean UPDATED_IS_OPEN = true;

    private static final String ENTITY_API_URL = "/api/parkings";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ParkingRepository parkingRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restParkingMockMvc;

    private Parking parking;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Parking createEntity(EntityManager em) {
        Parking parking = new Parking()
            .nom(DEFAULT_NOM)
            .nb_places(DEFAULT_NB_PLACES)
            .date_creation(DEFAULT_DATE_CREATION)
            .is_open(DEFAULT_IS_OPEN);
        return parking;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Parking createUpdatedEntity(EntityManager em) {
        Parking parking = new Parking()
            .nom(UPDATED_NOM)
            .nb_places(UPDATED_NB_PLACES)
            .date_creation(UPDATED_DATE_CREATION)
            .is_open(UPDATED_IS_OPEN);
        return parking;
    }

    @BeforeEach
    public void initTest() {
        parking = createEntity(em);
    }

    @Test
    @Transactional
    void createParking() throws Exception {
        int databaseSizeBeforeCreate = parkingRepository.findAll().size();
        // Create the Parking
        restParkingMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(parking)))
            .andExpect(status().isCreated());

        // Validate the Parking in the database
        List<Parking> parkingList = parkingRepository.findAll();
        assertThat(parkingList).hasSize(databaseSizeBeforeCreate + 1);
        Parking testParking = parkingList.get(parkingList.size() - 1);
        assertThat(testParking.getNom()).isEqualTo(DEFAULT_NOM);
        assertThat(testParking.getNb_places()).isEqualTo(DEFAULT_NB_PLACES);
        assertThat(testParking.getDate_creation()).isEqualTo(DEFAULT_DATE_CREATION);
        assertThat(testParking.getIs_open()).isEqualTo(DEFAULT_IS_OPEN);
    }

    @Test
    @Transactional
    void createParkingWithExistingId() throws Exception {
        // Create the Parking with an existing ID
        parking.setId(1L);

        int databaseSizeBeforeCreate = parkingRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restParkingMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(parking)))
            .andExpect(status().isBadRequest());

        // Validate the Parking in the database
        List<Parking> parkingList = parkingRepository.findAll();
        assertThat(parkingList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllParkings() throws Exception {
        // Initialize the database
        parkingRepository.saveAndFlush(parking);

        // Get all the parkingList
        restParkingMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(parking.getId().intValue())))
            .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM)))
            .andExpect(jsonPath("$.[*].nb_places").value(hasItem(DEFAULT_NB_PLACES)))
            .andExpect(jsonPath("$.[*].date_creation").value(hasItem(DEFAULT_DATE_CREATION.toString())))
            .andExpect(jsonPath("$.[*].is_open").value(hasItem(DEFAULT_IS_OPEN.booleanValue())));
    }

    @Test
    @Transactional
    void getParking() throws Exception {
        // Initialize the database
        parkingRepository.saveAndFlush(parking);

        // Get the parking
        restParkingMockMvc
            .perform(get(ENTITY_API_URL_ID, parking.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(parking.getId().intValue()))
            .andExpect(jsonPath("$.nom").value(DEFAULT_NOM))
            .andExpect(jsonPath("$.nb_places").value(DEFAULT_NB_PLACES))
            .andExpect(jsonPath("$.date_creation").value(DEFAULT_DATE_CREATION.toString()))
            .andExpect(jsonPath("$.is_open").value(DEFAULT_IS_OPEN.booleanValue()));
    }

    @Test
    @Transactional
    void getNonExistingParking() throws Exception {
        // Get the parking
        restParkingMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingParking() throws Exception {
        // Initialize the database
        parkingRepository.saveAndFlush(parking);

        int databaseSizeBeforeUpdate = parkingRepository.findAll().size();

        // Update the parking
        Parking updatedParking = parkingRepository.findById(parking.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedParking are not directly saved in db
        em.detach(updatedParking);
        updatedParking.nom(UPDATED_NOM).nb_places(UPDATED_NB_PLACES).date_creation(UPDATED_DATE_CREATION).is_open(UPDATED_IS_OPEN);

        restParkingMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedParking.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedParking))
            )
            .andExpect(status().isOk());

        // Validate the Parking in the database
        List<Parking> parkingList = parkingRepository.findAll();
        assertThat(parkingList).hasSize(databaseSizeBeforeUpdate);
        Parking testParking = parkingList.get(parkingList.size() - 1);
        assertThat(testParking.getNom()).isEqualTo(UPDATED_NOM);
        assertThat(testParking.getNb_places()).isEqualTo(UPDATED_NB_PLACES);
        assertThat(testParking.getDate_creation()).isEqualTo(UPDATED_DATE_CREATION);
        assertThat(testParking.getIs_open()).isEqualTo(UPDATED_IS_OPEN);
    }

    @Test
    @Transactional
    void putNonExistingParking() throws Exception {
        int databaseSizeBeforeUpdate = parkingRepository.findAll().size();
        parking.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restParkingMockMvc
            .perform(
                put(ENTITY_API_URL_ID, parking.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(parking))
            )
            .andExpect(status().isBadRequest());

        // Validate the Parking in the database
        List<Parking> parkingList = parkingRepository.findAll();
        assertThat(parkingList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchParking() throws Exception {
        int databaseSizeBeforeUpdate = parkingRepository.findAll().size();
        parking.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restParkingMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(parking))
            )
            .andExpect(status().isBadRequest());

        // Validate the Parking in the database
        List<Parking> parkingList = parkingRepository.findAll();
        assertThat(parkingList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamParking() throws Exception {
        int databaseSizeBeforeUpdate = parkingRepository.findAll().size();
        parking.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restParkingMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(parking)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Parking in the database
        List<Parking> parkingList = parkingRepository.findAll();
        assertThat(parkingList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateParkingWithPatch() throws Exception {
        // Initialize the database
        parkingRepository.saveAndFlush(parking);

        int databaseSizeBeforeUpdate = parkingRepository.findAll().size();

        // Update the parking using partial update
        Parking partialUpdatedParking = new Parking();
        partialUpdatedParking.setId(parking.getId());

        partialUpdatedParking.nb_places(UPDATED_NB_PLACES);

        restParkingMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedParking.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedParking))
            )
            .andExpect(status().isOk());

        // Validate the Parking in the database
        List<Parking> parkingList = parkingRepository.findAll();
        assertThat(parkingList).hasSize(databaseSizeBeforeUpdate);
        Parking testParking = parkingList.get(parkingList.size() - 1);
        assertThat(testParking.getNom()).isEqualTo(DEFAULT_NOM);
        assertThat(testParking.getNb_places()).isEqualTo(UPDATED_NB_PLACES);
        assertThat(testParking.getDate_creation()).isEqualTo(DEFAULT_DATE_CREATION);
        assertThat(testParking.getIs_open()).isEqualTo(DEFAULT_IS_OPEN);
    }

    @Test
    @Transactional
    void fullUpdateParkingWithPatch() throws Exception {
        // Initialize the database
        parkingRepository.saveAndFlush(parking);

        int databaseSizeBeforeUpdate = parkingRepository.findAll().size();

        // Update the parking using partial update
        Parking partialUpdatedParking = new Parking();
        partialUpdatedParking.setId(parking.getId());

        partialUpdatedParking.nom(UPDATED_NOM).nb_places(UPDATED_NB_PLACES).date_creation(UPDATED_DATE_CREATION).is_open(UPDATED_IS_OPEN);

        restParkingMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedParking.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedParking))
            )
            .andExpect(status().isOk());

        // Validate the Parking in the database
        List<Parking> parkingList = parkingRepository.findAll();
        assertThat(parkingList).hasSize(databaseSizeBeforeUpdate);
        Parking testParking = parkingList.get(parkingList.size() - 1);
        assertThat(testParking.getNom()).isEqualTo(UPDATED_NOM);
        assertThat(testParking.getNb_places()).isEqualTo(UPDATED_NB_PLACES);
        assertThat(testParking.getDate_creation()).isEqualTo(UPDATED_DATE_CREATION);
        assertThat(testParking.getIs_open()).isEqualTo(UPDATED_IS_OPEN);
    }

    @Test
    @Transactional
    void patchNonExistingParking() throws Exception {
        int databaseSizeBeforeUpdate = parkingRepository.findAll().size();
        parking.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restParkingMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, parking.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(parking))
            )
            .andExpect(status().isBadRequest());

        // Validate the Parking in the database
        List<Parking> parkingList = parkingRepository.findAll();
        assertThat(parkingList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchParking() throws Exception {
        int databaseSizeBeforeUpdate = parkingRepository.findAll().size();
        parking.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restParkingMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(parking))
            )
            .andExpect(status().isBadRequest());

        // Validate the Parking in the database
        List<Parking> parkingList = parkingRepository.findAll();
        assertThat(parkingList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamParking() throws Exception {
        int databaseSizeBeforeUpdate = parkingRepository.findAll().size();
        parking.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restParkingMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(parking)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Parking in the database
        List<Parking> parkingList = parkingRepository.findAll();
        assertThat(parkingList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteParking() throws Exception {
        // Initialize the database
        parkingRepository.saveAndFlush(parking);

        int databaseSizeBeforeDelete = parkingRepository.findAll().size();

        // Delete the parking
        restParkingMockMvc
            .perform(delete(ENTITY_API_URL_ID, parking.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Parking> parkingList = parkingRepository.findAll();
        assertThat(parkingList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
