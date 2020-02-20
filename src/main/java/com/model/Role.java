package com.model;

import javax.persistence.*;
import java.util.Set;

/**
 * <h1>Role model</h1>
 * This class handles the extraction and insertion of data into the table Role.
 * It also defines public keys. It also defines the relation the table Role has to the table Person.
 */
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

    /**
     * Returns the role id from the column role_id
     * @return int This returns the role id
     */
    public int getRole_id() { return role_id; }

    /**
     * Set a int value in the column role_id
     * @param role_id This is the only parameter to setRole_id method
     */
    public void setRole_id(int role_id) { this.role_id = role_id; }

    /**
     * Returns the role name from the column name
     * @return String This returns the role name.
     */
    public String getName() { return name; }

    /**
     * Set a string value in the column name
     * @param name This is the only parameter to setName method
     */
    public void setName(String name) { this.name = name; }

    public Set<Person> getPersonSet() {
        return personSet;
    }

    public void setPersonSet(Set<Person> personSet) {
        this.personSet = personSet;
    }
}
