package com.DAO;

import com.model.Applikation;
import com.model.Availability;
import com.model.Competence_Profile;
import com.repository.AvailabilityRepository;
import com.service.RecruitmentAppService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * This class is called ApplikationDAO
 */
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

    /**
     * The constructor of ApplikationDAO
     */
    public ApplikationDAO(){}

    /**
     * The constructor of ApplikationDAO with parameters
     * @param applikation This is the first parameter of the constructor
     * @param availability This is the second parameter of the constructor
     * @param competence_profile This is the third parameter of the constructor
     */
    public ApplikationDAO(Applikation applikation, Iterable<Availability> availability,Iterable<Competence_Profile> competence_profile) {
        try{
            addDataFromApplikation(applikation);
            addDataFromAvailability(availability);
            addDataFromCompetenceProfile(competence_profile);
        }
        catch(NullPointerException e){
            System.out.println(e.toString() + " in ApplikationDAO");
        }
        catch (NoSuchElementException e){
            System.out.println(e.toString() + " in ApplikationDAO");
        }

    }

    /**
     * This method is used to get the applikation date
     * @return String
     */
    public String getApplikation_date() {
        return applikation_date;
    }

    /**
     * This gets the work form
     * @return String
     */
    public String getWorkFrom() {
        return workFrom;
    }

    /**
     * This return the work to
     * @return String
     */
    public String getWorkTo() {
        return workTo;
    }

    /**
     * This returns the status of the application
     * @return String
     */
    public String getStatus() {
        return status;
    }

    /**
     * This returns the application id
     * @return int
     */
    public int getApplikationId() {return applikationId;}

    /**
     * This returns a competence profile map
     * @return HashMap
     */
    public HashMap<String, Integer> getCompetence_profileMap() {
        return competence_profileMap;
    }

    /**
     * This method adds data from the applikation
     * @param applikation This is only parameter of the method addDataFromApplikation
     */
    private void addDataFromApplikation(Applikation applikation) {
        if(applikation != null) {
            this.applikationId = applikation.getApplikationId();
            this.applikation_date = fixDateFormat(applikation.getApplikation_date());
            this.status = applikation.getStatus().getStatusName();
        }

    }

    /**
     * This returns a list of availability
     * @return ArrayList
     */
    public ArrayList<Availability> getAvailabilities() {
        return availabilities;
    }

    /**
     * This method adds data from all availability
     * @param availability This is the only parameter of the method addDataFromAvailability
     */
    private void addDataFromAvailability(Iterable <Availability> availability) {
        /*Iterator<Availability> test = availability.iterator();
        while(test.hasNext()){
            availabilities.add(test.next());
        }*/
        for(Availability avail: availability){
            availabilities.add(avail);
        }
    }

    /**
     * This method adds data from competence profile
     * @param competence_profile This is the only parameter of the method addDataFromCompetenceProfile
     */
    private void addDataFromCompetenceProfile(Iterable<Competence_Profile> competence_profile){
        /*Iterator iterator = competence_profile.iterator();
        while(iterator.hasNext()){
            Competence_Profile  currentCompetenceProfile = (Competence_Profile) iterator.next();
            String competenceName = currentCompetenceProfile.getCompetence().getCompetenceName();
            int years = currentCompetenceProfile.getYears();
            competence_profileMap.put(competenceName,years);
        }*/
        for(Competence_Profile cp : competence_profile){
            competence_profileMap.put(cp.getCompetence().getCompetenceName(),cp.getYears());
        }
    }

    /**
     * This method fixes the date format
     * @param date This is the only parameter to the method fixDateFormat
     * @return String
     */
    private String fixDateFormat(Date date){
      return date.toString().substring(0, 10);
    }
}
