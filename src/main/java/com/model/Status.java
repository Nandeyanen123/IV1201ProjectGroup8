package com.model;

import javax.persistence.*;
import java.util.Set;

/**
 * This is a class for the table status
 */
@Entity
@Table(name="status")
public class Status {

    @Id
    @Column(name = "status_id")
    private Integer statusId;

    @Column(name="name")
    private String statusName;


    @OneToMany(mappedBy = "status", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Applikation> applikationSet;

    /**
     * This method returns the status id
     * @return Integer returns the id
     */
    public Integer getStatusId() {
        return statusId;
    }

    /**
     * This method set a new value to status id
     * @param statusId This is the only parameter of setStatusId
     */
    public void setStatusId(Integer statusId) {
        this.statusId = statusId;
    }

    /**
     * This method returns the status name
     * @return String returns the name
     */
    public String getStatusName() {
        return statusName;
    }

    /**
     * This method set a new value to status name
     * @param statusName This is the only parameter of setStatusName
     */
    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    /**
     * This method returns set of applications connected to status
     * @return Set returns a set of applications
     */
    public Set<Applikation> getApplikationSet() {
        return applikationSet;
    }

    /**
     * This method set a new application set to status
     * @param applikationSet This is the only parameter of setApplikationSet
     */
    public void setApplikationSet(Set<Applikation> applikationSet) {
        this.applikationSet = applikationSet;
    }
}
