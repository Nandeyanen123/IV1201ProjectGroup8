package com.repository;

import com.model.Competence;
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

import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * This is the repository class for Competence.
 * It extends JpaRepository
 */
@Repository
public interface CompetenceRepository extends JpaRepository<Competence, Integer> {
    Competence findByCompetenceName(String competenceName);
}
