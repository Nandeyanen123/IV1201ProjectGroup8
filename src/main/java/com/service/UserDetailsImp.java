package com.service;

import com.model.Person;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

/**
 * This class implements UserDetails.
 *
 */
public class UserDetailsImp implements UserDetails {

    private Person person;

    /**
     * Sets the person member variable.
     * @param person The new person value that was sent to the constructor
     */
    public UserDetailsImp(Person person) {
        super();
        this.person = person;
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(new SimpleGrantedAuthority("Applicant"));
    }

    /**
     * This method gets the person password
     * @return String This is the person password
     */
    @Override
    public String getPassword() {
        return person.getPassword();
    }

    /**
     * This method gets the person username
     * @return String This is the person username
     */
    @Override
    public String getUsername() {
        return person.getUserName();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
