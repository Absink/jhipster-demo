package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.Parking2;
import com.mycompany.myapp.repository.Parking2Repository;
import com.mycompany.myapp.service.InternalService;
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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.mycompany.myapp.domain.Parking2}.
 */
@RestController
@RequestMapping("/api/parking-2-s")
@Transactional
public class Parking2Resource {

    private final Logger log = LoggerFactory.getLogger(Parking2Resource.class);

    private static final String ENTITY_NAME = "parking2";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final Parking2Repository parking2Repository;

    private final InternalService internalService;

    public Parking2Resource(Parking2Repository parking2Repository, InternalService internalService) {
        this.parking2Repository = parking2Repository;
        this.internalService = internalService;
    }

    /**
     * {@code POST  /parking-2-s} : Create a new parking2.
     *
     * @param parking2 the parking2 to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new parking2, or with status {@code 400 (Bad Request)} if the parking2 has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<Parking2> createParking2(@RequestBody Parking2 parking2) throws URISyntaxException {
        log.debug("REST request to save Parking2 : {}", parking2);
        if (parking2.getId() != null) {
            throw new BadRequestAlertException("A new parking2 cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Parking2 result = parking2Repository.save(parking2);
        return ResponseEntity
            .created(new URI("/api/parking-2-s/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /parking-2-s/:id} : Updates an existing parking2.
     *
     * @param id the id of the parking2 to save.
     * @param parking2 the parking2 to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated parking2,
     * or with status {@code 400 (Bad Request)} if the parking2 is not valid,
     * or with status {@code 500 (Internal Server Error)} if the parking2 couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Parking2> updateParking2(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Parking2 parking2
    ) throws URISyntaxException {
        log.debug("REST request to update Parking2 : {}, {}", id, parking2);
        if (parking2.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, parking2.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!parking2Repository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Parking2 result = parking2Repository.save(parking2);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, parking2.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /parking-2-s/:id} : Partial updates given fields of an existing parking2, field will ignore if it is null
     *
     * @param id the id of the parking2 to save.
     * @param parking2 the parking2 to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated parking2,
     * or with status {@code 400 (Bad Request)} if the parking2 is not valid,
     * or with status {@code 404 (Not Found)} if the parking2 is not found,
     * or with status {@code 500 (Internal Server Error)} if the parking2 couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Parking2> partialUpdateParking2(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Parking2 parking2
    ) throws URISyntaxException {
        log.debug("REST request to partial update Parking2 partially : {}, {}", id, parking2);
        if (parking2.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, parking2.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!parking2Repository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Parking2> result = parking2Repository
            .findById(parking2.getId())
            .map(existingParking2 -> {
                if (parking2.getNom() != null) {
                    existingParking2.setNom(parking2.getNom());
                }
                if (parking2.getNbPlaces() != null) {
                    existingParking2.setNbPlaces(parking2.getNbPlaces());
                }
                if (parking2.getDateCreation() != null) {
                    existingParking2.setDateCreation(parking2.getDateCreation());
                }
                if (parking2.getIsOpen() != null) {
                    existingParking2.setIsOpen(parking2.getIsOpen());
                }

                return existingParking2;
            })
            .map(parking2Repository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, parking2.getId().toString())
        );
    }

    /**
     * {@code GET  /parking-2-s} : get all the parking2s.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of parking2s in body.
     */
    @GetMapping("")
    public ResponseEntity<List<Parking2>> getAllParking2s(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Parking2s");
        Page<Parking2> page = parking2Repository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /parking-2-s/:id} : get the "id" parking2.
     *
     * @param id the id of the parking2 to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the parking2, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Parking2> getParking2(@PathVariable Long id) {
        log.debug("REST request to get Parking2 : {}", id);
        Optional<Parking2> parking2 = parking2Repository.findById(id);
        parking2.ifPresent(p -> {
            p.setIsOpen(!this.internalService.parkingIsFull(p));
        });
        return ResponseUtil.wrapOrNotFound(parking2);
    }

    /**
     * {@code DELETE  /parking-2-s/:id} : delete the "id" parking2.
     *
     * @param id the id of the parking2 to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteParking2(@PathVariable Long id) {
        log.debug("REST request to delete Parking2 : {}", id);
        parking2Repository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
