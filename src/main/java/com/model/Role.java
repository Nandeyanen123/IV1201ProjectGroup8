package com.model;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name="role")
public class Role {

    @Id
    @Column(name = "role_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int role_id;
    @Column(name="name")
    private String name;
    @OneToMany(mappedBy = "role", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<Person> personSet;

    public int getRole_id() { return role_id; }

    public void setRole_id(int role_id) { this.role_id = role_id; }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public Set<Person> getPersonSet() {
        return personSet;
    }

    public void setPersonSet(Set<Person> personSet) {
        this.personSet = personSet;
    }
}
