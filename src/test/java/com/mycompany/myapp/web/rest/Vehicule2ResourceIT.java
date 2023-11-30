package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Vehicule2;
import com.mycompany.myapp.domain.enumeration.Marque;
import com.mycompany.myapp.repository.Vehicule2Repository;
import jakarta.persistence.EntityManager;
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
 * Integration tests for the {@link Vehicule2Resource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class Vehicule2ResourceIT {

    private static final String DEFAULT_NOM = "AAAAAAAAAA";
    private static final String UPDATED_NOM = "BBBBBBBBBB";

    private static final Integer DEFAULT_PRIX = 1;
    private static final Integer UPDATED_PRIX = 2;

    private static final Integer DEFAULT_NB_CHEVAUX = 1;
    private static final Integer UPDATED_NB_CHEVAUX = 2;

    private static final Marque DEFAULT_MARQUE = Marque.RENAULT;
    private static final Marque UPDATED_MARQUE = Marque.PORSCHE;

    private static final String ENTITY_API_URL = "/api/vehicule-2-s";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private Vehicule2Repository vehicule2Repository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restVehicule2MockMvc;

    private Vehicule2 vehicule2;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Vehicule2 createEntity(EntityManager em) {
        Vehicule2 vehicule2 = new Vehicule2().nom(DEFAULT_NOM).prix(DEFAULT_PRIX).nbChevaux(DEFAULT_NB_CHEVAUX).marque(DEFAULT_MARQUE);
        return vehicule2;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Vehicule2 createUpdatedEntity(EntityManager em) {
        Vehicule2 vehicule2 = new Vehicule2().nom(UPDATED_NOM).prix(UPDATED_PRIX).nbChevaux(UPDATED_NB_CHEVAUX).marque(UPDATED_MARQUE);
        return vehicule2;
    }

    @BeforeEach
    public void initTest() {
        vehicule2 = createEntity(em);
    }

    @Test
    @Transactional
    void createVehicule2() throws Exception {
        int databaseSizeBeforeCreate = vehicule2Repository.findAll().size();
        // Create the Vehicule2
        restVehicule2MockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(vehicule2)))
            .andExpect(status().isCreated());

        // Validate the Vehicule2 in the database
        List<Vehicule2> vehicule2List = vehicule2Repository.findAll();
        assertThat(vehicule2List).hasSize(databaseSizeBeforeCreate + 1);
        Vehicule2 testVehicule2 = vehicule2List.get(vehicule2List.size() - 1);
        assertThat(testVehicule2.getNom()).isEqualTo(DEFAULT_NOM);
        assertThat(testVehicule2.getPrix()).isEqualTo(DEFAULT_PRIX);
        assertThat(testVehicule2.getNbChevaux()).isEqualTo(DEFAULT_NB_CHEVAUX);
        assertThat(testVehicule2.getMarque()).isEqualTo(DEFAULT_MARQUE);
    }

    @Test
    @Transactional
    void createVehicule2WithExistingId() throws Exception {
        // Create the Vehicule2 with an existing ID
        vehicule2.setId(1L);

        int databaseSizeBeforeCreate = vehicule2Repository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restVehicule2MockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(vehicule2)))
            .andExpect(status().isBadRequest());

        // Validate the Vehicule2 in the database
        List<Vehicule2> vehicule2List = vehicule2Repository.findAll();
        assertThat(vehicule2List).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllVehicule2s() throws Exception {
        // Initialize the database
        vehicule2Repository.saveAndFlush(vehicule2);

        // Get all the vehicule2List
        restVehicule2MockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(vehicule2.getId().intValue())))
            .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM)))
            .andExpect(jsonPath("$.[*].prix").value(hasItem(DEFAULT_PRIX)))
            .andExpect(jsonPath("$.[*].nbChevaux").value(hasItem(DEFAULT_NB_CHEVAUX)))
            .andExpect(jsonPath("$.[*].marque").value(hasItem(DEFAULT_MARQUE.toString())));
    }

    @Test
    @Transactional
    void getVehicule2() throws Exception {
        // Initialize the database
        vehicule2Repository.saveAndFlush(vehicule2);

        // Get the vehicule2
        restVehicule2MockMvc
            .perform(get(ENTITY_API_URL_ID, vehicule2.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(vehicule2.getId().intValue()))
            .andExpect(jsonPath("$.nom").value(DEFAULT_NOM))
            .andExpect(jsonPath("$.prix").value(DEFAULT_PRIX))
            .andExpect(jsonPath("$.nbChevaux").value(DEFAULT_NB_CHEVAUX))
            .andExpect(jsonPath("$.marque").value(DEFAULT_MARQUE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingVehicule2() throws Exception {
        // Get the vehicule2
        restVehicule2MockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingVehicule2() throws Exception {
        // Initialize the database
        vehicule2Repository.saveAndFlush(vehicule2);

        int databaseSizeBeforeUpdate = vehicule2Repository.findAll().size();

        // Update the vehicule2
        Vehicule2 updatedVehicule2 = vehicule2Repository.findById(vehicule2.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedVehicule2 are not directly saved in db
        em.detach(updatedVehicule2);
        updatedVehicule2.nom(UPDATED_NOM).prix(UPDATED_PRIX).nbChevaux(UPDATED_NB_CHEVAUX).marque(UPDATED_MARQUE);

        restVehicule2MockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedVehicule2.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedVehicule2))
            )
            .andExpect(status().isOk());

        // Validate the Vehicule2 in the database
        List<Vehicule2> vehicule2List = vehicule2Repository.findAll();
        assertThat(vehicule2List).hasSize(databaseSizeBeforeUpdate);
        Vehicule2 testVehicule2 = vehicule2List.get(vehicule2List.size() - 1);
        assertThat(testVehicule2.getNom()).isEqualTo(UPDATED_NOM);
        assertThat(testVehicule2.getPrix()).isEqualTo(UPDATED_PRIX);
        assertThat(testVehicule2.getNbChevaux()).isEqualTo(UPDATED_NB_CHEVAUX);
        assertThat(testVehicule2.getMarque()).isEqualTo(UPDATED_MARQUE);
    }

    @Test
    @Transactional
    void putNonExistingVehicule2() throws Exception {
        int databaseSizeBeforeUpdate = vehicule2Repository.findAll().size();
        vehicule2.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVehicule2MockMvc
            .perform(
                put(ENTITY_API_URL_ID, vehicule2.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(vehicule2))
            )
            .andExpect(status().isBadRequest());

        // Validate the Vehicule2 in the database
        List<Vehicule2> vehicule2List = vehicule2Repository.findAll();
        assertThat(vehicule2List).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchVehicule2() throws Exception {
        int databaseSizeBeforeUpdate = vehicule2Repository.findAll().size();
        vehicule2.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVehicule2MockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(vehicule2))
            )
            .andExpect(status().isBadRequest());

        // Validate the Vehicule2 in the database
        List<Vehicule2> vehicule2List = vehicule2Repository.findAll();
        assertThat(vehicule2List).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamVehicule2() throws Exception {
        int databaseSizeBeforeUpdate = vehicule2Repository.findAll().size();
        vehicule2.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVehicule2MockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(vehicule2)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Vehicule2 in the database
        List<Vehicule2> vehicule2List = vehicule2Repository.findAll();
        assertThat(vehicule2List).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateVehicule2WithPatch() throws Exception {
        // Initialize the database
        vehicule2Repository.saveAndFlush(vehicule2);

        int databaseSizeBeforeUpdate = vehicule2Repository.findAll().size();

        // Update the vehicule2 using partial update
        Vehicule2 partialUpdatedVehicule2 = new Vehicule2();
        partialUpdatedVehicule2.setId(vehicule2.getId());

        partialUpdatedVehicule2.prix(UPDATED_PRIX);

        restVehicule2MockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedVehicule2.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedVehicule2))
            )
            .andExpect(status().isOk());

        // Validate the Vehicule2 in the database
        List<Vehicule2> vehicule2List = vehicule2Repository.findAll();
        assertThat(vehicule2List).hasSize(databaseSizeBeforeUpdate);
        Vehicule2 testVehicule2 = vehicule2List.get(vehicule2List.size() - 1);
        assertThat(testVehicule2.getNom()).isEqualTo(DEFAULT_NOM);
        assertThat(testVehicule2.getPrix()).isEqualTo(UPDATED_PRIX);
        assertThat(testVehicule2.getNbChevaux()).isEqualTo(DEFAULT_NB_CHEVAUX);
        assertThat(testVehicule2.getMarque()).isEqualTo(DEFAULT_MARQUE);
    }

    @Test
    @Transactional
    void fullUpdateVehicule2WithPatch() throws Exception {
        // Initialize the database
        vehicule2Repository.saveAndFlush(vehicule2);

        int databaseSizeBeforeUpdate = vehicule2Repository.findAll().size();

        // Update the vehicule2 using partial update
        Vehicule2 partialUpdatedVehicule2 = new Vehicule2();
        partialUpdatedVehicule2.setId(vehicule2.getId());

        partialUpdatedVehicule2.nom(UPDATED_NOM).prix(UPDATED_PRIX).nbChevaux(UPDATED_NB_CHEVAUX).marque(UPDATED_MARQUE);

        restVehicule2MockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedVehicule2.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedVehicule2))
            )
            .andExpect(status().isOk());

        // Validate the Vehicule2 in the database
        List<Vehicule2> vehicule2List = vehicule2Repository.findAll();
        assertThat(vehicule2List).hasSize(databaseSizeBeforeUpdate);
        Vehicule2 testVehicule2 = vehicule2List.get(vehicule2List.size() - 1);
        assertThat(testVehicule2.getNom()).isEqualTo(UPDATED_NOM);
        assertThat(testVehicule2.getPrix()).isEqualTo(UPDATED_PRIX);
        assertThat(testVehicule2.getNbChevaux()).isEqualTo(UPDATED_NB_CHEVAUX);
        assertThat(testVehicule2.getMarque()).isEqualTo(UPDATED_MARQUE);
    }

    @Test
    @Transactional
    void patchNonExistingVehicule2() throws Exception {
        int databaseSizeBeforeUpdate = vehicule2Repository.findAll().size();
        vehicule2.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVehicule2MockMvc
            .perform(
                patch(ENTITY_API_URL_ID, vehicule2.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(vehicule2))
            )
            .andExpect(status().isBadRequest());

        // Validate the Vehicule2 in the database
        List<Vehicule2> vehicule2List = vehicule2Repository.findAll();
        assertThat(vehicule2List).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchVehicule2() throws Exception {
        int databaseSizeBeforeUpdate = vehicule2Repository.findAll().size();
        vehicule2.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVehicule2MockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(vehicule2))
            )
            .andExpect(status().isBadRequest());

        // Validate the Vehicule2 in the database
        List<Vehicule2> vehicule2List = vehicule2Repository.findAll();
        assertThat(vehicule2List).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamVehicule2() throws Exception {
        int databaseSizeBeforeUpdate = vehicule2Repository.findAll().size();
        vehicule2.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVehicule2MockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(vehicule2))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Vehicule2 in the database
        List<Vehicule2> vehicule2List = vehicule2Repository.findAll();
        assertThat(vehicule2List).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteVehicule2() throws Exception {
        // Initialize the database
        vehicule2Repository.saveAndFlush(vehicule2);

        int databaseSizeBeforeDelete = vehicule2Repository.findAll().size();

        // Delete the vehicule2
        restVehicule2MockMvc
            .perform(delete(ENTITY_API_URL_ID, vehicule2.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Vehicule2> vehicule2List = vehicule2Repository.findAll();
        assertThat(vehicule2List).hasSize(databaseSizeBeforeDelete - 1);
    }
}
