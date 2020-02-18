package com.model;

import javax.persistence.*;

@Entity
@Table(name="role")
public class Role {

    @Id
    @Column(name = "role_id")
    private Integer role_id;

    @Column(name = "name")
    private String name;

    public Integer getId(){
        return role_id;
    }

    public String getName() {
        return name;
    }
}
