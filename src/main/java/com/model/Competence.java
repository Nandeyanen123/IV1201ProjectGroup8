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

    public Integer getCompetenceId() {
        return competenceId;
    }

    public void setCompetenceId(Integer competenceId) {
        this.competenceId = competenceId;
    }

    public String getCompetenceName() {
        return competenceName;
    }

    public void setCompetenceName(String competenceName) {
        this.competenceName = competenceName;
    }

}
