package com.DAO;

import com.model.Applikation;
import com.model.Availability;
import com.model.Competence_Profile;
import com.repository.AvailabilityRepository;
import com.service.RecruitmentAppService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

//TODO Comments

@Component
public class ApplikationDAO {
    int applikationId = 0;
    String applikation_date = "";
    String workFrom = "";
    String workTo = "";
    String status = "";
    Iterator<Competence_Profile> competenceProfileIterator;
    HashMap<String, Integer> competence_profileMap = new HashMap<String, Integer>();

    ArrayList<Availability> availabilities = new ArrayList<>();

    public ApplikationDAO(){}
    public ApplikationDAO(Applikation applikation, Iterable<Availability> availability,Iterable<Competence_Profile> competence_profile) {
        try{
            addDataFromApplikation(applikation);
            addDataFromAvailability(availability);
            addDataFromCompetencProfile(competence_profile);
        }
        catch(NullPointerException e){
            System.out.println(e.toString() + " in ApplikationDAO");
        }
        catch (NoSuchElementException e){
            System.out.println(e.toString() + " in ApplikationDAO");
        }

    }
    public String getApplikation_date() {
        return applikation_date;
    }

    public String getWorkFrom() {
        return workFrom;
    }

    public String getWorkTo() {
        return workTo;
    }

    public String getStatus() {
        return status;
    }

    public int getApplikationId() {return applikationId;}

    public HashMap<String, Integer> getCompetence_profileMap() {
        return competence_profileMap;
    }

    private void addDataFromApplikation(Applikation applikation) {
        if(applikation != null) {
            this.applikationId = applikation.getApplikationId();
            this.applikation_date = fixDateFormat(applikation.getApplikation_date());
            this.status = applikation.getStatus().getStatusName();
        }

    }

    public ArrayList<Availability> getAvailabilities() {
        return availabilities;
    }


    private void addDataFromAvailability(Iterable <Availability> availability) {
        Iterator<Availability> test = availability.iterator();
        while(test.hasNext()){
            availabilities.add(test.next());
        }
    }

    private void addDataFromCompetencProfile(Iterable<Competence_Profile> competence_profile){
        Iterator iterator = competence_profile.iterator();
        while(iterator.hasNext()){
            Competence_Profile  currentCompetenceProfile = (Competence_Profile) iterator.next();
            String competenceName = currentCompetenceProfile.getCompetence().getCompetenceName();
            int years = currentCompetenceProfile.getYears();
            competence_profileMap.put(competenceName,years);
        }
    }

    private String fixDateFormat(Date date){
      return date.toString().substring(0, 10);
    }
}
