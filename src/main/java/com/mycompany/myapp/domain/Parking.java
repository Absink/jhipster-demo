package com.mycompany.myapp.domain;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Parking.
 */
@Entity
@Table(name = "parking")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Parking implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "nom")
    private String nom;

    @Column(name = "nb_places")
    private Integer nb_places;

    @Column(name = "date_creation")
    private LocalDate date_creation;

    @Column(name = "is_open")
    private Boolean is_open;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Parking id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return this.nom;
    }

    public Parking nom(String nom) {
        this.setNom(nom);
        return this;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public Integer getNb_places() {
        return this.nb_places;
    }

    public Parking nb_places(Integer nb_places) {
        this.setNb_places(nb_places);
        return this;
    }

    public void setNb_places(Integer nb_places) {
        this.nb_places = nb_places;
    }

    public LocalDate getDate_creation() {
        return this.date_creation;
    }

    public Parking date_creation(LocalDate date_creation) {
        this.setDate_creation(date_creation);
        return this;
    }

    public void setDate_creation(LocalDate date_creation) {
        this.date_creation = date_creation;
    }

    public Boolean getIs_open() {
        return this.is_open;
    }

    public Parking is_open(Boolean is_open) {
        this.setIs_open(is_open);
        return this;
    }

    public void setIs_open(Boolean is_open) {
        this.is_open = is_open;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Parking)) {
            return false;
        }
        return getId() != null && getId().equals(((Parking) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Parking{" +
            "id=" + getId() +
            ", nom='" + getNom() + "'" +
            ", nb_places=" + getNb_places() +
            ", date_creation='" + getDate_creation() + "'" +
            ", is_open='" + getIs_open() + "'" +
            "}";
    }
}
