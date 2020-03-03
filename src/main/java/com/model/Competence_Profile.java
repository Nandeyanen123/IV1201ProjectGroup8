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

    /**
     * This is the constructor of the class Competence Profile. It sets new values to person, competence and years.
     * @param p This is the first parameter of the constructor Competence_Profile
     * @param competence This is the second parameter of the constructor Competence_Profile
     * @param yearInt This is the third parameter of the constructor Competence_Profile
     */
    public Competence_Profile(Person p, Competence competence, int yearInt) {
        this.person = p;
        this.competence = competence;
        this.years = yearInt;
    }

    /**
     * This is used to get the competence profile id
     * @return Integer returns the competence profile id
     */
    public Integer getCompetence_profileId() {
        return competence_profileId;
    }

    /**
     * This is used to get the competence profile person
     * @return Integer returns the person
     */
    public Person getPerson() {
        return person;
    }

    /**
     * This is used to get the competence profile competence
     * @return Integer returns the competence
     */
    public Competence getCompetence() {
        return competence;
    }

    /**
     * This is used to get the competence profile years
     * @return Integer returns the years
     */
    public Integer getYears() {
        return years;
    }

    /**
     * This method is used to set years in the column
     * @param years This is the only parameter for the method
     */
    public void setYears(Integer years) {
        this.years = years;
    }

}
