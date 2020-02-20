package com.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="competence")
public class Competence {
    @Id
    @Column(name="competence_id")
    private Integer competenceId;

    @Column(name="name")
    private String competenceName;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Competence_Profile> competence_profiles= new ArrayList<>();

}
