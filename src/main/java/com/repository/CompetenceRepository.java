package com.repository;

import com.model.Competence;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * This is the repository class for Competence.
 * It extends JpaRepository
 */
@Repository
public interface CompetenceRepository extends JpaRepository<Competence, Integer> {
    Competence findByCompetenceName(String competenceName);
}
