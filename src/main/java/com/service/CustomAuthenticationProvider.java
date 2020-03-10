package com.service;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.model.Person;
import com.model.Role;
import com.repository.PersonRepository;
import com.repository.RoleRepository;
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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import javax.transaction.Transactional;


/**
 * This class implements AuthenticationProvider.
 */
@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {
    @Autowired
    RecruitmentAppService appService;

    @Autowired
    PasswordEncoder passwordEncoder;

    private final String APPLICANT = "applicant";
    private final String RECRUITER = "recruiter";

    /**
     * The constructor of the class.
     */
    public CustomAuthenticationProvider() {
        super();
    }

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
    @Transactional
    public Authentication authenticate(final Authentication authentication) throws AuthenticationException {
        final String name = authentication.getName();
        final String password = authentication.getCredentials().toString();
        final Authentication auth;

        final List<GrantedAuthority> grantedAuths = new ArrayList<>();
        System.out.println("Transaction ongoing? : "+TransactionSynchronizationManager.isActualTransactionActive());
        String encodedPassword=passwordEncoder.encode(password);
        try{
            Person person = appService.findPerson(name);
            int roleId = person.getRoleId();

            if (person.getUserName().equals(name) && passwordEncoder.matches(password,person.getPassword())) {
                addAllRolesFromDB(grantedAuths, roleId);
                final UserDetails principal = new User(name, encodedPassword, grantedAuths);
                auth = new UsernamePasswordAuthenticationToken(principal, encodedPassword, grantedAuths);
                return auth;
            }
        }
        catch(NullPointerException e){
            System.out.println(e.toString() + " in CustomAuthenticationProvider");
            throw new UsernameNotFoundException("Login error");
        }

        return null;
        /*
        if(person==null || person.getRoleId() == null)
            throw new UsernameNotFoundException("Login error");


        }
        else
            throw new UsernameNotFoundException("Login error");  */
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

    /**
     * Helper method that adds right roles to and GrantedAuthority
     * @param grantedAuths all granted auths
     * @param personRoleId role id of person that wants auth
     */
    @Transactional
    private void addAllRolesFromDB(List<GrantedAuthority> grantedAuths, int personRoleId){
        Iterable<Role> rolesIterable = appService.getAllRoles();
        for(Role role : rolesIterable){
            if(role.getRole_id() == personRoleId)
                grantedAuths.add(new SimpleGrantedAuthority(role.getName()));
        }
        /*Iterator roles = rolesIterable.iterator();
        while(roles.hasNext()){
            Role role = (Role) roles.next();
            if(role.getRole_id() == personRoleId)
                grantedAuths.add(new SimpleGrantedAuthority(role.getName()));
        }*/
    }

}