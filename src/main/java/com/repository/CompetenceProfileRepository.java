package com.repository;

import com.model.Competence;
import com.model.Competence_Profile;
import com.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

/**
 * This is the repository class for Competence profile.
 * It extends JpaRepository
 */
@Repository
public interface CompetenceProfileRepository extends JpaRepository<Competence_Profile, Integer> {
    Iterable<Competence_Profile> findAllByPersonId(int id);
    Competence_Profile findByPersonAndCompetence(Person person, Competence competence);

    ArrayList<Competence_Profile> findAllByPersonOrderByCompetence(Person person);
}
