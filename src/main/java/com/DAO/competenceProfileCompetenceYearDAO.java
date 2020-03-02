package com.DAO;

import com.model.Competence;
import com.model.Competence_Profile;
import com.repository.CompetenceProfileRepository;
import com.repository.CompetenceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class competenceProfileCompetenceYearDAO {
    @Autowired
    CompetenceRepository competenceRepository;
    @Autowired
    CompetenceProfileRepository competenceProfileRepository;

    public competenceProfileCompetenceYearDAO(){ }
    public Map<String,Integer> getCompetenceNameAndYear(int id, Iterable<Competence_Profile> competence_Profile, Iterable<Competence> competence){
        Map<String,Integer> myMap = new HashMap<String,Integer>();
        Iterator<Competence_Profile> competenceProfileIterator = competence_Profile.iterator();

        while(competenceProfileIterator.hasNext()){
            Competence_Profile currCompProfile = competenceProfileIterator.next();

            int competenceId = currCompProfile.getCompetence().getCompetenceId();
            String nameOfCompetence = "";
            Iterator<Competence> competenceIterator = competence.iterator();
            while(competenceIterator.hasNext()){
                Competence curCompetence = competenceIterator.next();

                if(curCompetence.getCompetenceId() == competenceId) {
                    System.out.println("comp id == " + competenceId);
                    nameOfCompetence = curCompetence.getCompetenceName();
                    int years = currCompProfile.getYears();
                    myMap.put(nameOfCompetence, years);
                    break;
                }
            }
        }
        return myMap;
    }
}