package com.repository;

import com.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * This is the repository class for Person.
 * It extends JpaRepository
 */
@Repository
public interface PersonRepository extends JpaRepository<Person, Integer> {
    Person findByUserName(String username);
    Person findBySsn(String ssn);
    Person findByEmail(String email);
    Person findById(int id);
}
