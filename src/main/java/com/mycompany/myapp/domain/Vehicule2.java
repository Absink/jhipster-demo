package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mycompany.myapp.domain.enumeration.Marque;
import jakarta.persistence.*;
import java.io.Serializable;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Vehicule2.
 */
@Entity
@Table(name = "vehicule_2")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Vehicule2 implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "nom")
    private String nom;

    @Column(name = "prix")
    private Integer prix;

    @Column(name = "nb_chevaux")
    private Integer nbChevaux;

    @Enumerated(EnumType.STRING)
    @Column(name = "marque")
    private Marque marque;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "vehicule2s" }, allowSetters = true)
    private Parking2 parking2;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Vehicule2 id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return this.nom;
    }

    public Vehicule2 nom(String nom) {
        this.setNom(nom);
        return this;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public Integer getPrix() {
        return this.prix;
    }

    public Vehicule2 prix(Integer prix) {
        this.setPrix(prix);
        return this;
    }

    public void setPrix(Integer prix) {
        this.prix = prix;
    }

    public Integer getNbChevaux() {
        return this.nbChevaux;
    }

    public Vehicule2 nbChevaux(Integer nbChevaux) {
        this.setNbChevaux(nbChevaux);
        return this;
    }

    public void setNbChevaux(Integer nbChevaux) {
        this.nbChevaux = nbChevaux;
    }

    public Marque getMarque() {
        return this.marque;
    }

    public Vehicule2 marque(Marque marque) {
        this.setMarque(marque);
        return this;
    }

    public void setMarque(Marque marque) {
        this.marque = marque;
    }

    public Parking2 getParking2() {
        return this.parking2;
    }

    public void setParking2(Parking2 parking2) {
        this.parking2 = parking2;
    }

    public Vehicule2 parking2(Parking2 parking2) {
        this.setParking2(parking2);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Vehicule2)) {
            return false;
        }
        return getId() != null && getId().equals(((Vehicule2) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Vehicule2{" +
            "id=" + getId() +
            ", nom='" + getNom() + "'" +
            ", prix=" + getPrix() +
            ", nbChevaux=" + getNbChevaux() +
            ", marque='" + getMarque() + "'" +
            "}";
    }
}
