package com.DAO;

import com.model.Competence;
import com.model.Competence_Profile;
import com.repository.CompetenceProfileRepository;
import com.repository.CompetenceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * This class is called CompetenceProfileCompetenceYearDAO
 */
@Component
public class CompetenceProfileCompetenceYearDAO {

    /**
     * This is the constructor of the class
     */
    public CompetenceProfileCompetenceYearDAO(){ }

    /**
     * This is used to get the competence name and year.
     * @param id This is the first parameter of the method
     * @param competence_Profile This is the second parameter of the method
     * @param competence This is the third parameter of the method
     * @return Map This returns a map with a string and integer values
     */
    public Map<String,Integer> getCompetenceNameAndYear(Iterable<Competence_Profile> competence_Profile){
        Map<String,Integer> competenceMap = new HashMap<String,Integer>();
        for(Competence_Profile cp : competence_Profile){
            competenceMap.put(cp.getCompetence().getCompetenceName(),cp.getYears());
        }
        return competenceMap;
    }
}
