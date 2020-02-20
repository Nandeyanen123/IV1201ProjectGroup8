package com.service;
import java.util.ArrayList;
import java.util.List;

import com.model.Person;
import com.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {
    @Autowired
    PersonRepository personRepository;


    public CustomAuthenticationProvider() {
        super();
    }

    // API

    @Override
    public Authentication authenticate(final Authentication authentication) throws AuthenticationException {
        final String name = authentication.getName();
        final String password = authentication.getCredentials().toString();

        Person person = personRepository.findByUserName(name);
        if(person==null)
            throw new UsernameNotFoundException("Login error");

        if (person.getUserName().equals(name) && password.equals(person.getPassword())) {

            //# TODO Fix list with all roles

            final List<GrantedAuthority> grantedAuths = new ArrayList<>();
            grantedAuths.add(new SimpleGrantedAuthority("applicant"));

            final UserDetails principal = new User(name, password, grantedAuths);

            final Authentication auth = new UsernamePasswordAuthenticationToken(principal, password, grantedAuths);

            return auth;
        }
        else
            throw new UsernameNotFoundException("Login error");

        //  return new UserDetailsImp(person);

        /*if (name.equals("admin") && password.equals("system")) {
            final List<GrantedAuthority> grantedAuths = new ArrayList<>();
            grantedAuths.add(new SimpleGrantedAuthority("applicant"));
            final UserDetails principal = new User(name, password, grantedAuths);
            final Authentication auth = new UsernamePasswordAuthenticationToken(principal, password, grantedAuths);
            return auth;
        } else {
            return null;
        }

         */
    }

    @Override
    public boolean supports(final Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }

}