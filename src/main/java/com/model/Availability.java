package com.model;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * This is the class for the table availability
 */
@Entity
@Table(name="availability")
public class Availability {

    @Id
    @Column(name = "availability_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer availabilityId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "person_id", updatable = false)
    private Person person;

    @Column(name = "from_date")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date fromDate;

    @Column(name = "to_date")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date toDate;

    public Availability(){}
    public Availability(Person person, Date from_date, Date to_date){
        this.person = person;
        this.fromDate = from_date;
        this.toDate = to_date;
    }

    public Integer getAvailabilityId() {
        return availabilityId;
    }

    public void setAvailabilityId(Integer availabilityId) {
        this.availabilityId = availabilityId;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public Date getFromDate() {
        return fromDate;
    }

    public void setFromDate(Date fromDate) {
        this.fromDate = fromDate;
    }

    public Date getToDate() {
        return toDate;
    }

    public void setToDate(Date toDate) {
        this.toDate = toDate;
    }

    public String getFromDateFixed(){
        return fromDate.toString().substring(0, 10);
    }


    public String getToDateFixed(){
        return toDate.toString().substring(0, 10);
    }
}
