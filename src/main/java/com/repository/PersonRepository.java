package com.repository;

import com.model.Person;
import com.model.Role;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface PersonRepository extends JpaRepository<Person, Integer> {

    Person findByUserName(String username);
    Person findBySsn(String ssn);
    Person findByEmail(String email);
}
