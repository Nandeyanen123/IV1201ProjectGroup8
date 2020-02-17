package com.config;


import com.repository.PersonRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories(basePackageClasses = PersonRepository.class)
public class SecurityConfig {
}
