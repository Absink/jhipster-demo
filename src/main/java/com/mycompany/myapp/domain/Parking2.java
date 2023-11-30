package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Parking2.
 */
@Entity
@Table(name = "parking_2")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Parking2 implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "nom")
    private String nom;

    @Column(name = "nb_places")
    private Integer nbPlaces;

    @Column(name = "date_creation")
    private LocalDate dateCreation;

    @Column(name = "is_open")
    private Boolean isOpen;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "parking2")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "parking2" }, allowSetters = true)
    private Set<Vehicule2> vehicule2s = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Parking2 id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return this.nom;
    }

    public Parking2 nom(String nom) {
        this.setNom(nom);
        return this;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public Integer getNbPlaces() {
        return this.nbPlaces;
    }

    public Parking2 nbPlaces(Integer nbPlaces) {
        this.setNbPlaces(nbPlaces);
        return this;
    }

    public void setNbPlaces(Integer nbPlaces) {
        this.nbPlaces = nbPlaces;
    }

    public LocalDate getDateCreation() {
        return this.dateCreation;
    }

    public Parking2 dateCreation(LocalDate dateCreation) {
        this.setDateCreation(dateCreation);
        return this;
    }

    public void setDateCreation(LocalDate dateCreation) {
        this.dateCreation = dateCreation;
    }

    public Boolean getIsOpen() {
        return this.isOpen;
    }

    public Parking2 isOpen(Boolean isOpen) {
        this.setIsOpen(isOpen);
        return this;
    }

    public void setIsOpen(Boolean isOpen) {
        this.isOpen = isOpen;
    }

    public Set<Vehicule2> getVehicule2s() {
        return this.vehicule2s;
    }

    public void setVehicule2s(Set<Vehicule2> vehicule2s) {
        if (this.vehicule2s != null) {
            this.vehicule2s.forEach(i -> i.setParking2(null));
        }
        if (vehicule2s != null) {
            vehicule2s.forEach(i -> i.setParking2(this));
        }
        this.vehicule2s = vehicule2s;
    }

    public Parking2 vehicule2s(Set<Vehicule2> vehicule2s) {
        this.setVehicule2s(vehicule2s);
        return this;
    }

    public Parking2 addVehicule2(Vehicule2 vehicule2) {
        this.vehicule2s.add(vehicule2);
        vehicule2.setParking2(this);
        return this;
    }

    public Parking2 removeVehicule2(Vehicule2 vehicule2) {
        this.vehicule2s.remove(vehicule2);
        vehicule2.setParking2(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Parking2)) {
            return false;
        }
        return getId() != null && getId().equals(((Parking2) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Parking2{" +
            "id=" + getId() +
            ", nom='" + getNom() + "'" +
            ", nbPlaces=" + getNbPlaces() +
            ", dateCreation='" + getDateCreation() + "'" +
            ", isOpen='" + getIsOpen() + "'" +
            "}";
    }
}
