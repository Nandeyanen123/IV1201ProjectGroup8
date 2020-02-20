package com.model;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name="role")
public class Role {

    @Id
    @Column(name = "role_id")
    private Integer role_id;

    @Column(name = "name")
    private String name;


    @OneToMany(mappedBy = "role", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Person> personSet;

    public Integer getId(){
        return role_id;
    }

    public String getName() {
        return name;
    }
}
