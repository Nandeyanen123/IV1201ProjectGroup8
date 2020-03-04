package com.repository;

import com.model.Applikation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * This is the repository class for Status.
 * It extends JpaRepository
 */
@Repository
public interface StatusRepository extends JpaRepository<Applikation, Integer> {

}
