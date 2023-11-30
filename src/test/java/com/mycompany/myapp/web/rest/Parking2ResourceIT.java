package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Parking2;
import com.mycompany.myapp.repository.Parking2Repository;
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
 * Integration tests for the {@link Parking2Resource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class Parking2ResourceIT {

    private static final String DEFAULT_NOM = "AAAAAAAAAA";
    private static final String UPDATED_NOM = "BBBBBBBBBB";

    private static final Integer DEFAULT_NB_PLACES = 1;
    private static final Integer UPDATED_NB_PLACES = 2;

    private static final LocalDate DEFAULT_DATE_CREATION = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_CREATION = LocalDate.now(ZoneId.systemDefault());

    private static final Boolean DEFAULT_IS_OPEN = false;
    private static final Boolean UPDATED_IS_OPEN = true;

    private static final String ENTITY_API_URL = "/api/parking-2-s";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private Parking2Repository parking2Repository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restParking2MockMvc;

    private Parking2 parking2;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Parking2 createEntity(EntityManager em) {
        Parking2 parking2 = new Parking2()
            .nom(DEFAULT_NOM)
            .nbPlaces(DEFAULT_NB_PLACES)
            .dateCreation(DEFAULT_DATE_CREATION)
            .isOpen(DEFAULT_IS_OPEN);
        return parking2;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Parking2 createUpdatedEntity(EntityManager em) {
        Parking2 parking2 = new Parking2()
            .nom(UPDATED_NOM)
            .nbPlaces(UPDATED_NB_PLACES)
            .dateCreation(UPDATED_DATE_CREATION)
            .isOpen(UPDATED_IS_OPEN);
        return parking2;
    }

    @BeforeEach
    public void initTest() {
        parking2 = createEntity(em);
    }

    @Test
    @Transactional
    void createParking2() throws Exception {
        int databaseSizeBeforeCreate = parking2Repository.findAll().size();
        // Create the Parking2
        restParking2MockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(parking2)))
            .andExpect(status().isCreated());

        // Validate the Parking2 in the database
        List<Parking2> parking2List = parking2Repository.findAll();
        assertThat(parking2List).hasSize(databaseSizeBeforeCreate + 1);
        Parking2 testParking2 = parking2List.get(parking2List.size() - 1);
        assertThat(testParking2.getNom()).isEqualTo(DEFAULT_NOM);
        assertThat(testParking2.getNbPlaces()).isEqualTo(DEFAULT_NB_PLACES);
        assertThat(testParking2.getDateCreation()).isEqualTo(DEFAULT_DATE_CREATION);
        assertThat(testParking2.getIsOpen()).isEqualTo(DEFAULT_IS_OPEN);
    }

    @Test
    @Transactional
    void createParking2WithExistingId() throws Exception {
        // Create the Parking2 with an existing ID
        parking2.setId(1L);

        int databaseSizeBeforeCreate = parking2Repository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restParking2MockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(parking2)))
            .andExpect(status().isBadRequest());

        // Validate the Parking2 in the database
        List<Parking2> parking2List = parking2Repository.findAll();
        assertThat(parking2List).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllParking2s() throws Exception {
        // Initialize the database
        parking2Repository.saveAndFlush(parking2);

        // Get all the parking2List
        restParking2MockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(parking2.getId().intValue())))
            .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM)))
            .andExpect(jsonPath("$.[*].nbPlaces").value(hasItem(DEFAULT_NB_PLACES)))
            .andExpect(jsonPath("$.[*].dateCreation").value(hasItem(DEFAULT_DATE_CREATION.toString())))
            .andExpect(jsonPath("$.[*].isOpen").value(hasItem(DEFAULT_IS_OPEN.booleanValue())));
    }

    @Test
    @Transactional
    void getParking2() throws Exception {
        // Initialize the database
        parking2Repository.saveAndFlush(parking2);

        // Get the parking2
        restParking2MockMvc
            .perform(get(ENTITY_API_URL_ID, parking2.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(parking2.getId().intValue()))
            .andExpect(jsonPath("$.nom").value(DEFAULT_NOM))
            .andExpect(jsonPath("$.nbPlaces").value(DEFAULT_NB_PLACES))
            .andExpect(jsonPath("$.dateCreation").value(DEFAULT_DATE_CREATION.toString()))
            .andExpect(jsonPath("$.isOpen").value(DEFAULT_IS_OPEN.booleanValue()));
    }

    @Test
    @Transactional
    void getNonExistingParking2() throws Exception {
        // Get the parking2
        restParking2MockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingParking2() throws Exception {
        // Initialize the database
        parking2Repository.saveAndFlush(parking2);

        int databaseSizeBeforeUpdate = parking2Repository.findAll().size();

        // Update the parking2
        Parking2 updatedParking2 = parking2Repository.findById(parking2.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedParking2 are not directly saved in db
        em.detach(updatedParking2);
        updatedParking2.nom(UPDATED_NOM).nbPlaces(UPDATED_NB_PLACES).dateCreation(UPDATED_DATE_CREATION).isOpen(UPDATED_IS_OPEN);

        restParking2MockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedParking2.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedParking2))
            )
            .andExpect(status().isOk());

        // Validate the Parking2 in the database
        List<Parking2> parking2List = parking2Repository.findAll();
        assertThat(parking2List).hasSize(databaseSizeBeforeUpdate);
        Parking2 testParking2 = parking2List.get(parking2List.size() - 1);
        assertThat(testParking2.getNom()).isEqualTo(UPDATED_NOM);
        assertThat(testParking2.getNbPlaces()).isEqualTo(UPDATED_NB_PLACES);
        assertThat(testParking2.getDateCreation()).isEqualTo(UPDATED_DATE_CREATION);
        assertThat(testParking2.getIsOpen()).isEqualTo(UPDATED_IS_OPEN);
    }

    @Test
    @Transactional
    void putNonExistingParking2() throws Exception {
        int databaseSizeBeforeUpdate = parking2Repository.findAll().size();
        parking2.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restParking2MockMvc
            .perform(
                put(ENTITY_API_URL_ID, parking2.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(parking2))
            )
            .andExpect(status().isBadRequest());

        // Validate the Parking2 in the database
        List<Parking2> parking2List = parking2Repository.findAll();
        assertThat(parking2List).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchParking2() throws Exception {
        int databaseSizeBeforeUpdate = parking2Repository.findAll().size();
        parking2.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restParking2MockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(parking2))
            )
            .andExpect(status().isBadRequest());

        // Validate the Parking2 in the database
        List<Parking2> parking2List = parking2Repository.findAll();
        assertThat(parking2List).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamParking2() throws Exception {
        int databaseSizeBeforeUpdate = parking2Repository.findAll().size();
        parking2.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restParking2MockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(parking2)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Parking2 in the database
        List<Parking2> parking2List = parking2Repository.findAll();
        assertThat(parking2List).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateParking2WithPatch() throws Exception {
        // Initialize the database
        parking2Repository.saveAndFlush(parking2);

        int databaseSizeBeforeUpdate = parking2Repository.findAll().size();

        // Update the parking2 using partial update
        Parking2 partialUpdatedParking2 = new Parking2();
        partialUpdatedParking2.setId(parking2.getId());

        partialUpdatedParking2.isOpen(UPDATED_IS_OPEN);

        restParking2MockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedParking2.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedParking2))
            )
            .andExpect(status().isOk());

        // Validate the Parking2 in the database
        List<Parking2> parking2List = parking2Repository.findAll();
        assertThat(parking2List).hasSize(databaseSizeBeforeUpdate);
        Parking2 testParking2 = parking2List.get(parking2List.size() - 1);
        assertThat(testParking2.getNom()).isEqualTo(DEFAULT_NOM);
        assertThat(testParking2.getNbPlaces()).isEqualTo(DEFAULT_NB_PLACES);
        assertThat(testParking2.getDateCreation()).isEqualTo(DEFAULT_DATE_CREATION);
        assertThat(testParking2.getIsOpen()).isEqualTo(UPDATED_IS_OPEN);
    }

    @Test
    @Transactional
    void fullUpdateParking2WithPatch() throws Exception {
        // Initialize the database
        parking2Repository.saveAndFlush(parking2);

        int databaseSizeBeforeUpdate = parking2Repository.findAll().size();

        // Update the parking2 using partial update
        Parking2 partialUpdatedParking2 = new Parking2();
        partialUpdatedParking2.setId(parking2.getId());

        partialUpdatedParking2.nom(UPDATED_NOM).nbPlaces(UPDATED_NB_PLACES).dateCreation(UPDATED_DATE_CREATION).isOpen(UPDATED_IS_OPEN);

        restParking2MockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedParking2.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedParking2))
            )
            .andExpect(status().isOk());

        // Validate the Parking2 in the database
        List<Parking2> parking2List = parking2Repository.findAll();
        assertThat(parking2List).hasSize(databaseSizeBeforeUpdate);
        Parking2 testParking2 = parking2List.get(parking2List.size() - 1);
        assertThat(testParking2.getNom()).isEqualTo(UPDATED_NOM);
        assertThat(testParking2.getNbPlaces()).isEqualTo(UPDATED_NB_PLACES);
        assertThat(testParking2.getDateCreation()).isEqualTo(UPDATED_DATE_CREATION);
        assertThat(testParking2.getIsOpen()).isEqualTo(UPDATED_IS_OPEN);
    }

    @Test
    @Transactional
    void patchNonExistingParking2() throws Exception {
        int databaseSizeBeforeUpdate = parking2Repository.findAll().size();
        parking2.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restParking2MockMvc
            .perform(
                patch(ENTITY_API_URL_ID, parking2.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(parking2))
            )
            .andExpect(status().isBadRequest());

        // Validate the Parking2 in the database
        List<Parking2> parking2List = parking2Repository.findAll();
        assertThat(parking2List).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchParking2() throws Exception {
        int databaseSizeBeforeUpdate = parking2Repository.findAll().size();
        parking2.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restParking2MockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(parking2))
            )
            .andExpect(status().isBadRequest());

        // Validate the Parking2 in the database
        List<Parking2> parking2List = parking2Repository.findAll();
        assertThat(parking2List).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamParking2() throws Exception {
        int databaseSizeBeforeUpdate = parking2Repository.findAll().size();
        parking2.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restParking2MockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(parking2)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Parking2 in the database
        List<Parking2> parking2List = parking2Repository.findAll();
        assertThat(parking2List).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteParking2() throws Exception {
        // Initialize the database
        parking2Repository.saveAndFlush(parking2);

        int databaseSizeBeforeDelete = parking2Repository.findAll().size();

        // Delete the parking2
        restParking2MockMvc
            .perform(delete(ENTITY_API_URL_ID, parking2.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Parking2> parking2List = parking2Repository.findAll();
        assertThat(parking2List).hasSize(databaseSizeBeforeDelete - 1);
    }
}
