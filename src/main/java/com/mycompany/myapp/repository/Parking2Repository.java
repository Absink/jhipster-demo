package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Parking2;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Parking2 entity.
 */
@SuppressWarnings("unused")
@Repository
public interface Parking2Repository extends JpaRepository<Parking2, Long> {}
