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

    /**
     * This method returns the availability id
     * @return Integer returns the id
     */
    public Integer getAvailabilityId() {
        return availabilityId;
    }

    /**
     * This method sets the availability id
     * @param availabilityId This is the only parameter of the method setAvailabilityId
     */
    public void setAvailabilityId(Integer availabilityId) {
        this.availabilityId = availabilityId;
    }

    /**
     * This method returns the availability person
     * @return Person returns the person
     */
    public Person getPerson() {
        return person;
    }

    /**
     * This method sets the availability person
     * @param person This is the only parameter of the method setPerson
     */
    public void setPerson(Person person) {
        this.person = person;
    }

    /**
     * This method returns the availability from date
     * @return Date returns the from date
     */
    public Date getFromDate() {
        return fromDate;
    }

    /**
     * This method sets the availability from date
     * @param fromDate This is the only parameter of the method setFromDate
     */
    public void setFromDate(Date fromDate) {
        this.fromDate = fromDate;
    }

    /**
     * This method returns the availability to date
     * @return Date returns the to date
     */
    public Date getToDate() {
        return toDate;
    }

    /**
     * This method sets the availability to date
     * @param toDate This is the only parameter of the method setToDate
     */
    public void setToDate(Date toDate) {
        this.toDate = toDate;
    }

    /**
     * This method returns the availability from date as a string
     * @return String returns the date
     */
    public String getFromDateFixed(){
        return fromDate.toString().substring(0, 10);
    }

    /**
     * This method returns the availability to date as a string
     * @return String returns the date
     */
    public String getToDateFixed(){
        return toDate.toString().substring(0, 10);
    }
}
