package com.repository;

import com.model.Person;
import com.model.Role;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * This is the repository class for Person.
 * It extends JpaRepository
 */
@Repository
public interface PersonRepository extends JpaRepository<Person, Integer> {
    Person findByUserName(String username);
    Person findBySsn(String ssn);
    Person findByEmail(String email);
}
