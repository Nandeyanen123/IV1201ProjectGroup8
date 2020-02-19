package com.config;

import com.service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.web.util.matcher.AndRequestMatcher;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

/**
 * This class AppSecurityConfig extends WebSecurityConfigAdapter
 */
@Configuration
@EnableWebSecurity
public class AppSecurityConfig extends WebSecurityConfigurerAdapter {



    @Autowired
    private CustomUserDetailsService userDetailsService;

    /**
     * This method makes a new DaoAuthenticationProvider variable.
     * Then it sets the variable user detail and encodes the password
     * @return AuthenticationProvider
     */
    @Bean
    public AuthenticationProvider authProvider()
    {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(new BCryptPasswordEncoder());
        return provider;
    }

    /**
     * This method is used to configure the HttpSecurity variables settings.
     * @param http This is the only parameter this method takes
     * @throws Exception when it doesn't work
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
         http.csrf()
                 .disable()
                 .authorizeRequests()
                 .antMatchers("/", "/register/**", "/error")
                    .permitAll()
                 .anyRequest().authenticated()
                 .and()
                 .formLogin()
                 .loginPage("/login").permitAll()
                .and().logout().invalidateHttpSession(true)
                .clearAuthentication(true)
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .logoutSuccessUrl("/logout-success").permitAll();
    }
}
