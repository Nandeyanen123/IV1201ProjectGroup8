package com.model;

import javax.persistence.*;

@Entity
@Table(name = "role")
public class Role {

    @Column(name = "role_id")
    private Integer id;

    @Column(name = "name")
    private String roleName;

    public Integer getId() { return id; }

    public void setId(Integer id) { this.id = id; }

    public String getRoleName() { return roleName; }

    public void setRoleName(String roleName) { this.roleName = roleName;}

}
