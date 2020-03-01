package com.model;

import org.springframework.stereotype.Component;

import javax.persistence.*;

/**
 * This is a class for the table competence_profile.
 */
@Entity
@Table(name="competence_profile")
public class Competence_Profile {

    @Id
    @Column(name="competence_profile_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer competence_profileId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "person_id", insertable = false, updatable = false)
    private Person person;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "competence_id", insertable = false, updatable = false)
    private Competence competence;

    @Column(name="years_of_experience")
    private Integer years;
}
