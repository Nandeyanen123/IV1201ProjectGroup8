package com.model;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * This is the class for the table applikation.
 */
@Entity
@Table(name="applikation")
public class Applikation {
    @Id
    @Column(name="applikation_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer applikationId;

    @Column(name="applikation_date")
    private Date applikation_date;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="status")
    private Status status;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="person", updatable = false)
    private Person person;

    public Applikation(){}
    public Applikation(Person person, Date now) {
        this.person = person;
        this.applikation_date = now;
        Status newStatus = new Status();
        newStatus.setStatusId(3);
        newStatus.setStatusName("Unhandled");
        this.status = newStatus;
    }

    /**
     * This method returns the application id
     * @return Integer the id of the applikation
     */
    public Integer getApplikationId() {
        return applikationId;
    }

    /**
     * This method sets the application id
     * @param applikationId This is the only parameter for the method SetApplikationId
     */
    public void setApplikationId(Integer applikationId) {
        this.applikationId = applikationId;
    }

    /**
     * This method returns application date
     * @return Date the date of the application
     */
    public Date getApplikation_date() {
        return applikation_date;
    }

    /**
     * This method sets the date of the application
     * @param applikation_date This is the only parameter of the method setApplikation_date
     */
    public void setApplikation_date(Date applikation_date) {
        this.applikation_date = applikation_date;
    }

    /**
     * This method returns application status
     * @return Status the status of the application
     */
    public Status getStatus() {
        return status;
    }

    /**
     * This method sets the status of the application
     * @param status This is the only parameter of the method setStatus
     */
    public void setStatus(Status status) {
        this.status = status;
    }

    /**
     * This method returns application person
     * @return Person the person of the application
     */
    public Person getPerson() {
        return person;
    }

    /**
     * This method set the person of the application
     * @param person This is the only parameter of the method setPerson
     */
    public void setPerson(Person person) {
        this.person = person;
    }

    /**
     * This method returns application date as a string
     * @return String the date of the application
     */
    public String getDateFixed(){
        return applikation_date.toString().substring(0, 10);
    }
}
