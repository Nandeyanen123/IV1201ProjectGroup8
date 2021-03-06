package com.repository;

import com.model.Applikation;
import com.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;


/**
 * This is the repository class for Applikation.
 * It extends JpaRepository
 */
@Repository
public interface ApplikationRepository  extends JpaRepository<Applikation, Integer> {
    Applikation findByPerson(Person person);
    Applikation findByPersonId(int id);
    Applikation findApplikationByApplikationId(int id);
    ArrayList<Applikation> findAll();
}


