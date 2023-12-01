package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.Parking2;
import com.mycompany.myapp.domain.User;
import com.mycompany.myapp.service.dto.ParkingDTO;
import com.mycompany.myapp.service.dto.UserDTO;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

/**
 * Mapper for the entity {@link User} and its DTO called {@link UserDTO}.
 *
 * Normal mappers are generated using MapStruct, this one is hand-coded as MapStruct
 * support is still in beta, and requires a manual step with an IDE.
 */
@Service
public class ParkingMapper {

    public List<ParkingDTO> parkingsToParkingDTOs(List<Parking2> parkings) {
        return parkings.stream().filter(Objects::nonNull).map(this::parkingToParkingDTO).collect(Collectors.toList());
    }

    public ParkingDTO parkingToParkingDTO(Parking2 parking) {
        return new ParkingDTO(parking);
    }
}
