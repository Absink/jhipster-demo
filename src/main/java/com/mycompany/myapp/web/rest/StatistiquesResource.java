package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.Vehicule2;
import com.mycompany.myapp.domain.enumeration.Marque;
import com.mycompany.myapp.repository.Vehicule2Repository;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.PaginationUtil;

/**
 * StatistiquesResource controller
 */
@RestController
@RequestMapping("/api/statistiques")
public class StatistiquesResource {

    private final Logger log = LoggerFactory.getLogger(StatistiquesResource.class);

    private final Vehicule2Repository vehicule2Repository;

    public StatistiquesResource(Vehicule2Repository vehicule2Repository) {
        this.vehicule2Repository = vehicule2Repository;
    }

    /**
     * GET vehiculesByMarque
     */
    @GetMapping("/vehicules/{marque}")
    public ResponseEntity<List<Vehicule2>> vehiculesByMarque(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        @PathVariable Marque marque
    ) {
        log.debug("REST request to get a page of Vehicule filter by marque");
        List<Vehicule2> vehicules = new ArrayList<Vehicule2>();
        for (Vehicule2 vehicule : vehicule2Repository.findAll()) {
            if (vehicule.getMarque().equals(marque)) vehicules.add(vehicule);
        }
        Page<Vehicule2> page = new PageImpl<>(vehicules);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
