package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.Vehicule2;
import com.mycompany.myapp.repository.Vehicule2Repository;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.mycompany.myapp.domain.Vehicule2}.
 */
@RestController
@RequestMapping("/api/vehicule-2-s")
@Transactional
public class Vehicule2Resource {

    private final Logger log = LoggerFactory.getLogger(Vehicule2Resource.class);

    private static final String ENTITY_NAME = "vehicule2";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final Vehicule2Repository vehicule2Repository;

    public Vehicule2Resource(Vehicule2Repository vehicule2Repository) {
        this.vehicule2Repository = vehicule2Repository;
    }

    /**
     * {@code POST  /vehicule-2-s} : Create a new vehicule2.
     *
     * @param vehicule2 the vehicule2 to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new vehicule2, or with status {@code 400 (Bad Request)} if the vehicule2 has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<Vehicule2> createVehicule2(@RequestBody Vehicule2 vehicule2) throws URISyntaxException {
        log.debug("REST request to save Vehicule2 : {}", vehicule2);
        if (vehicule2.getId() != null) {
            throw new BadRequestAlertException("A new vehicule2 cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Vehicule2 result = vehicule2Repository.save(vehicule2);
        return ResponseEntity
            .created(new URI("/api/vehicule-2-s/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /vehicule-2-s/:id} : Updates an existing vehicule2.
     *
     * @param id the id of the vehicule2 to save.
     * @param vehicule2 the vehicule2 to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated vehicule2,
     * or with status {@code 400 (Bad Request)} if the vehicule2 is not valid,
     * or with status {@code 500 (Internal Server Error)} if the vehicule2 couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Vehicule2> updateVehicule2(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Vehicule2 vehicule2
    ) throws URISyntaxException {
        log.debug("REST request to update Vehicule2 : {}, {}", id, vehicule2);
        if (vehicule2.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, vehicule2.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!vehicule2Repository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Vehicule2 result = vehicule2Repository.save(vehicule2);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, vehicule2.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /vehicule-2-s/:id} : Partial updates given fields of an existing vehicule2, field will ignore if it is null
     *
     * @param id the id of the vehicule2 to save.
     * @param vehicule2 the vehicule2 to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated vehicule2,
     * or with status {@code 400 (Bad Request)} if the vehicule2 is not valid,
     * or with status {@code 404 (Not Found)} if the vehicule2 is not found,
     * or with status {@code 500 (Internal Server Error)} if the vehicule2 couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Vehicule2> partialUpdateVehicule2(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Vehicule2 vehicule2
    ) throws URISyntaxException {
        log.debug("REST request to partial update Vehicule2 partially : {}, {}", id, vehicule2);
        if (vehicule2.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, vehicule2.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!vehicule2Repository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Vehicule2> result = vehicule2Repository
            .findById(vehicule2.getId())
            .map(existingVehicule2 -> {
                if (vehicule2.getNom() != null) {
                    existingVehicule2.setNom(vehicule2.getNom());
                }
                if (vehicule2.getPrix() != null) {
                    existingVehicule2.setPrix(vehicule2.getPrix());
                }
                if (vehicule2.getNbChevaux() != null) {
                    existingVehicule2.setNbChevaux(vehicule2.getNbChevaux());
                }
                if (vehicule2.getMarque() != null) {
                    existingVehicule2.setMarque(vehicule2.getMarque());
                }

                return existingVehicule2;
            })
            .map(vehicule2Repository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, vehicule2.getId().toString())
        );
    }

    /**
     * {@code GET  /vehicule-2-s} : get all the vehicule2s.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of vehicule2s in body.
     */
    @GetMapping("")
    public ResponseEntity<List<Vehicule2>> getAllVehicule2s(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Vehicule2s");
        Page<Vehicule2> page = vehicule2Repository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /vehicule-2-s/:id} : get the "id" vehicule2.
     *
     * @param id the id of the vehicule2 to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the vehicule2, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Vehicule2> getVehicule2(@PathVariable Long id) {
        log.debug("REST request to get Vehicule2 : {}", id);
        Optional<Vehicule2> vehicule2 = vehicule2Repository.findById(id);
        return ResponseUtil.wrapOrNotFound(vehicule2);
    }

    /**
     * {@code DELETE  /vehicule-2-s/:id} : delete the "id" vehicule2.
     *
     * @param id the id of the vehicule2 to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVehicule2(@PathVariable Long id) {
        log.debug("REST request to delete Vehicule2 : {}", id);
        vehicule2Repository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
