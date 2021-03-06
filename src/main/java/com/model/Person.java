package com.model;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Set;

/**
 * <h1>Person model</h1>
 * This class handles the extraction and input of data
 * into the database table person.
 */

@Entity
@Table(name="person")
public class Person {

    @Id
    @Column(name="person_id")
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Integer id;

    @Pattern(regexp = "^[a-zA-ZåäöÅÄÖ]*$", message = "Use only letters")
    @Column(name="name")
    @Size(min=1, max = 45 , message = "Please fill out a name (max 45 chars)")
    @NotNull
    private String name;

    @Pattern(regexp = "^[a-zA-ZåäöÅÄÖ]*$", message = "Use only letters")
    @Column(name="surname")
    @Size(min=1, max = 45, message = "Please fill out a surname (max 45 chars)")
    @NotNull
    private String surName;

    //@Pattern(regexp="^(19|20)?[0-9]{6}[- ]?[0-9]{4}$", message="Use format 19000101-0101")
    @Pattern(regexp = "^[0-9]*$" , message = "Use format yymmddxxxx")
    @Size(min = 1 , message = "Please enter ssn")
    @NotNull
    @Column(name="ssn", unique = true)
    private String ssn;

    @NotNull
    @Email(message = "Please enter your email")
    @Size(min = 1 , message = "Please enter your email")
    @Column(name="email")
    private String email;

    @NotNull
    @Size(min = 10, max = 70, message="Password needs to be at least 10 characters long")
    @Column(name="password")
    private String password;

    @Column(name="role_id")
    private Integer roleId;

    @Pattern(regexp = "^[a-zA-Z0-9åäöÅÄÖ]*$")
    @NotNull
    @Size(min = 5, max = 45)
    @Column(name="username", unique = true)
    private String userName;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name= "role_id", nullable = false,updatable = false,insertable = false)
    private Role role;

    @OneToOne(mappedBy = "person", fetch = FetchType.LAZY)
    private Applikation applikation;

    @OneToMany(mappedBy = "person", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Availability> availabilitySet;

    /**
     *
     * @return all CompetenceProfiels that is associated with this.person
     */
    public Set<Competence_Profile> getCompetence_profileSet() {
        return competence_profileSet;
    }

    @OneToMany(mappedBy = "person", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Competence_Profile> competence_profileSet;

    /**
     *
     * @return all Availability that is associated with this.person
     */
    public Set<Availability> getAvailabilitySet() {
        return availabilitySet;
    }

    /**
     * Returns id from the column person_id
     * @return Integer This returns the person id nr.
     */
    public Integer getId() {
        return id;
    }

    /**
     * Set a integer value in the column id
     * @param id This is the only parameter in the setId method
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * Returns a persons first name from the column name
     * @return String This returns the person name
     */
    public String getName() {
        return name;
    }

    /**
     * Set a string value in the column name
     * @param name This is the only parameter in the setName method
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns a persons surname from the column surname
     * @return String This returns the surname
     */
    public String getSurName() {
        return surName;
    }

    /**
     * Set a string value in the column surname
     * @param surName This is the only parameter in the setSurName method
     */
    public void setSurName(String surName) {
        this.surName = surName;
    }

    /**
     * Returns a person social security nr from the column ssn
     * @return String This returns the ssn nr.
     */
    public String getSsn() {
        return ssn;
    }

    /**
     * Set a string value in the column ssn
     * @param ssn This is the only parameter in the setSsn method
     */
    public void setSsn(String ssn) {
        this.ssn = ssn;
    }

    /**
     * Returns a person email adress from the column email
     * @return String This returns the email adress.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Set a string value in the column email.
     * @param email This is the only parameter in the setEmail method
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Returns a person password from the column password
     * @return String This returns the password.
     */
    public String getPassword() {
        return password;
    }

    /**
     * Set a string value inside the column password
     * @param password This is the only parameter in the setPassword method
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Returns a person role id from the column role
     * @return Integer This returns the role id.
     */
    public Integer getRoleId() {
        return roleId;
    }

    /**
     * Set a integer value inside the column role
     * @param roleId This is the only parameter in the setRoleId method
     */
    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    /**
     * Returns a person username from the column username
     * @return String This returns the username
     */
    public String getUserName() {
        return userName;
    }

    /**
     * Set a string value inside the column username
     * @param userName This is the only parameter in the setUserName method
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * Returns the a role variable that is connected to the table Role
     * @return Role this returns the role
     */
    public Role getRole() {
        return role;
    }

    /**
     * Set a new role value to the user.
     * @param role This is the only parameter in the setRole method
     */
    public void setRole(Role role) {
        this.role = role;
    }
}
