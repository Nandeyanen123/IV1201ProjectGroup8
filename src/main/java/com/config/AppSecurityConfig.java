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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class AppSecurityConfig extends WebSecurityConfigurerAdapter {


    @Autowired
    private CustomUserDetailsService userDetailsService;
    @Bean
    public AuthenticationProvider authProvider()
    {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(NoOpPasswordEncoder.getInstance());
        return provider;
    }

   /* @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/","/register/**","/error").permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .loginPage("/login").defaultSuccessUrl("/profile")
                .failureUrl("/login?error=true")
                .permitAll()
                .and()
                .logout()
                .invalidateHttpSession(true)
                .permitAll().and()
                .csrf()
                .disable();


    }*/
}
