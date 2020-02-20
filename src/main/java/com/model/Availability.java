package com.model;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name="availability")
public class Availability {

    @Id
    @Column(name = "availability_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer availabilityId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "person_id", insertable = false, updatable = false)
    private Integer personId;

    @Column(name = "from_date")
    private Date fromDate;

    @Column(name = "to_date")
    private Date toDate;

}
