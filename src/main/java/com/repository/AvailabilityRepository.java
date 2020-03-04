package com.repository;

import com.model.Applikation;
import com.model.Availability;
import com.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * This is the repository class for Competence profile.
 * It extends JpaRepository
 */
@Repository
public interface AvailabilityRepository extends JpaRepository<Availability, Integer> {
    Availability findByPerson(Person person);
}
