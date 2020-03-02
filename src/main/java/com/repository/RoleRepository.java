package com.repository;

import com.model.Person;
import com.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * This is the repository class for Role
 * It extends CrudRepository
 */
@Repository
public interface RoleRepository extends CrudRepository<Role, Integer> {

    Optional<Role> findById(int role_id);
}
