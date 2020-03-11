package com.repository;

import com.model.Availability;
import com.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

/**
 * This is the repository class for Availability.
 * It extends JpaRepository
 */
@Repository
public interface AvailabilityRepository extends JpaRepository<Availability, Integer> {
    Iterable <Availability> findAllByPersonId(Integer person_id);
    Availability findById(int id);
    ArrayList<Availability> getAvailabilitiesByPersonOrderByFromDate(Person person);

    @Query(value = "SELECT DISTINCT person_id from availability " +
            "WHERE availability.from_date <= ?1 " +
            "AND availability.to_date >= ?2",
            nativeQuery = true)
    ArrayList<Integer> getAllPersonIdsFromDates(@Param("from_date") String fromDate, @Param("to_date") String toDate);

    @Query(value = "SELECT DISTINCT person_id FROM availability", nativeQuery = true)
    ArrayList<Integer> getAllPersonId();


}
