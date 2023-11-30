package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Vehicule2;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Vehicule2 entity.
 */
@SuppressWarnings("unused")
@Repository
public interface Vehicule2Repository extends JpaRepository<Vehicule2, Long> {}
