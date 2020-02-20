package com.model;

import javax.persistence.*;
import java.util.Date;

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
}
