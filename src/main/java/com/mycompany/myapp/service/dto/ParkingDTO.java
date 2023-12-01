package com.mycompany.myapp.service.dto;

import com.mycompany.myapp.domain.Parking2;
import com.mycompany.myapp.domain.User;
import java.io.Serializable;
import java.time.LocalDate;

/**
 * A DTO representing a user, with only the public attributes.
 */
public class ParkingDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private String nom;
    private Integer nbPlaces;
    private LocalDate dateCreation;
    private Boolean isOpen;

    private Integer nbPlacesDisponibles;
    private int nb_vehicules;

    public ParkingDTO() {}

    public ParkingDTO(Parking2 parking) {
        this.id = parking.getId();
        this.nom = parking.getNom();
        this.nbPlaces = parking.getNbPlaces();
        this.dateCreation = parking.getDateCreation();
        this.isOpen = parking.getIsOpen();
        this.nb_vehicules = parking.getVehicule2s().size();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public Integer getNbPlaces() {
        return nbPlaces;
    }

    public void setNbPlaces(Integer nbPlaces) {
        this.nbPlaces = nbPlaces;
    }

    public LocalDate getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(LocalDate dateCreation) {
        this.dateCreation = dateCreation;
    }

    public Boolean getIsOpen() {
        return isOpen;
    }

    public void setIsOpen(Boolean isOpen) {
        this.isOpen = isOpen;
    }

    public Integer getNbPlacesDisponibles() {
        return nbPlacesDisponibles;
    }

    public void setNbPlacesDisponibles(Integer nbPlacesDisponibles) {
        this.nbPlacesDisponibles = nbPlacesDisponibles;
    }

    public int getNb_vehicules() {
        return nb_vehicules;
    }

    public void setNb_vehicules(int nb_vehicules) {
        this.nb_vehicules = nb_vehicules;
    }
}
