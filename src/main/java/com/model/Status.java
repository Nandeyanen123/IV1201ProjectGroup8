package com.model;

import javax.persistence.*;
import java.util.Set;

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
}
