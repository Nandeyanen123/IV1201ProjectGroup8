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


/**
 * This class implements AuthenticationProvider.
 */
@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {
    @Autowired
    PersonRepository personRepository;

    /**
     * The constructor of the class.
     */
    public CustomAuthenticationProvider() {
        super();
    }

    // API

    /**
     * This method returns authentication a user has. It first looks to see if the user exists in the database and
     * if it's password is equal to the password in the database.
     * It then grants the user authentication depending on it's role.
     * @param authentication This is the only parameter in the method
     * @return Authentication
     * @throws AuthenticationException if finding authentication doesn't work the n it throws AuthenticationException
     * @throws UsernameNotFoundException if it doesn't find the username then it throws UsernameNotFoundException
     */
    @Override
    public Authentication authenticate(final Authentication authentication) throws AuthenticationException {
        final String name = authentication.getName();
        final String password = authentication.getCredentials().toString();

        Person person = personRepository.findByUserName(name);
        if(person==null)
            throw new UsernameNotFoundException("Login error");

        if (person.getUserName().equals(name) && password.equals(person.getPassword())) {

            //# TODO Fix list with all roles
            //final String role = person.getRole().getName();

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

    /**
     * This method checks if the authentication is equal to the authentication the user has.
     * @param authentication This is the only parameter in the method.
     * @return This returns a boolean
     */
    @Override
    public boolean supports(final Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }

}