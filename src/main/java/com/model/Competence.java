package com.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * This is the class for the table competence
 */
@Entity
@Table(name="competence")
public class Competence {
    @Id
    @Column(name="competence_id")
    private int competenceId;

    @Column(name="name")
    private String competenceName;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Competence_Profile> competence_profiles= new ArrayList<>();

    /**
     * This is used to get the competence id.
     * @return Integer this returns the competence id.
     */
    public Integer getCompetenceId() {
        return competenceId;
    }

    /**
     * This is used to set a new competence id.
     * @param competenceId This is the only parameter
     */
    public void setCompetenceId(Integer competenceId) {
        this.competenceId = competenceId;
    }

    /**
     * This is used to get the competence name.
     * @return String this returns the competence name.
     */
    public String getCompetenceName() {
        return competenceName;
    }

    /**
     * This is used to set a new competence name
     * @param competenceName This is the only parameter
     */
    public void setCompetenceName(String competenceName) {
        this.competenceName = competenceName;
    }

}
