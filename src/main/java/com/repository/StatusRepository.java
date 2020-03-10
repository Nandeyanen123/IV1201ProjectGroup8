package com.repository;

import com.model.Applikation;
import com.model.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

/**
 * This is the repository class for Status.
 * It extends JpaRepository
 */
@Repository
public interface StatusRepository extends JpaRepository<Status, Integer> {
    ArrayList<Status> findAll();
}
