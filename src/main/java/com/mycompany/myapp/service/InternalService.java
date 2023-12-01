package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.Parking2;
import com.mycompany.myapp.service.dto.ParkingDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class InternalService {

    private final Logger log = LoggerFactory.getLogger(InternalService.class);

    public boolean parkingIsFull(Parking2 parking) {
        return parking.getVehicule2s().size() == parking.getNbPlaces();
    }

    public int getFreePlacesByParking(Parking2 parking) {
        if (parking.getVehicule2s().size() == 0) return parking.getNbPlaces(); else return (
            parking.getNbPlaces() - parking.getVehicule2s().size()
        );
    }

    public ParkingDTO addInfos(ParkingDTO parkingDTO) {
        parkingDTO.setNbPlacesDisponibles(parkingDTO.getNbPlaces() - parkingDTO.getNb_vehicules());
        return parkingDTO;
    }
}
