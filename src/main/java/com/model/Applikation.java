package com.model;

import javax.persistence.*;
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
    @JoinColumn(name="status", insertable = false, updatable = false)
    private Status status;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="person", insertable = false, updatable = false)
    private Person person;

    public Integer getApplikationId() {
        return applikationId;
    }

    public void setApplikationId(Integer applikationId) {
        this.applikationId = applikationId;
    }

    public Date getApplikation_date() {
        return applikation_date;
    }

    public void setApplikation_date(Date applikation_date) {
        this.applikation_date = applikation_date;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }
}
