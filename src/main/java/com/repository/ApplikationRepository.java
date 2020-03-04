package com.repository;

import com.model.Applikation;
import com.model.Competence;
import com.model.Competence_Profile;
import com.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


/**
 * This is the repository class for Competence profile.
 * It extends JpaRepository
 */
@Repository
public interface ApplikationRepository  extends JpaRepository<Applikation, Integer> {
    Applikation findByPerson(Person person);
}


