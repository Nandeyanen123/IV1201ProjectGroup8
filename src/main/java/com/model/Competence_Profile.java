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
    @JoinColumn(name = "person_id")
    private Person person;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "competence_id")
    private Competence competence;

    @Column(name="years_of_experience")
    private Integer years;

    public Competence_Profile(){}

    public Competence_Profile(Person p, Competence competence, int yearInt) {
        this.person = p;
        this.competence = competence;
        this.years = yearInt;
    }

    public Integer getCompetence_profileId() {
        return competence_profileId;
    }

    public Person getPerson() {
        return person;
    }

    public Competence getCompetence() {
        return competence;
    }

    public Integer getYears() {
        return years;
    }

    public void setYears(Integer years) {
        this.years = years;
    }

}
