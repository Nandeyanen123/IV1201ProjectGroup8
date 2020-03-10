package com.service;

import com.DAO.CompetenceProfileCompetenceYearDAO;
import com.model.*;
import com.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.support.SessionStatus;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

/**
 * This class is called RecruitmentAppService
 * It has methods of all the services the app offers.
 */
@Transactional
@Service
public class RecruitmentAppService {
    @Autowired
    private ApplikationRepository appRepo;
    @Autowired
    private AvailabilityRepository availabilityRepo;
    @Autowired
    private CompetenceProfileRepository competenceProfileRepo;
    @Autowired
    private CompetenceRepository competenceRepo;
    @Autowired
    private PersonRepository personRepo;
    @Autowired
    private PersonValidator personValidator;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private RoleRepository roleRepo;
    @Autowired
    private StatusRepository statusRepo;
    @Autowired
    private AvailabilityValidator availabilityValidator;

    /**
     * This method adds a new person
     * @param person This is the first parameter of the method addNewPerson
     * @param result This is the second parameter of the method addNewPerson
     * @param status This is the third parameter of the method addNewPerson
     * @return BindingResult returns the result of adding a new person
     */
    public BindingResult addNewPerson(@Valid Person person, BindingResult result, SessionStatus status) {
        // To Create own validate
        personValidator.validate(person, result);
        if(result.hasErrors()){
            return result;
        }else {
            String encodedPassword = passwordEncoder.encode(person.getPassword());
            person.setPassword(encodedPassword);
            person.setRoleId(2);
            personRepo.save(person);
            status.setComplete();
            return result;
        }
    }

    /**
     * This method returns all people
     * @return Iterable returns an iterable of person
     */
    public Iterable<Person> getAllPeople(){
        return personRepo.findAll();
    }

    /**
     * This method finds the user by using their name
     * @param userName This is the only parameter of FindPerson
     * @return Person returns the person with the name matching the parameter
     */
    public Person findPerson(String userName){
        return personRepo.findByUserName(userName);
    }

    /**
     * This method finds the user by using their ssn
     * @param ssn This is the only parameter of the method findPersonBySsn
     * @return Person returns the person with the ssn matching the parameter
     */
    public Person findPersonBySsn(String ssn){return personRepo.findBySsn(ssn);}

    /**
     * This method finds the user by using their email
     * @param email This is the only parameter of the method findPersonByEmail
     * @return Person returns the person with the email matching the email
     */
    public Person findPersonByEmail(String email){return personRepo.findByEmail(email);}

    /**
     * This method updates the user profile
     * @param personFromFrom This is the first parameter of the method profileUpdate
     * @param username This is the second parameter of the method profileUpdate
     * @param result This is the third parameter of the method profileUpdate
     * @param status This is the fourth parameter of the method profileUpdate
     * @return BindingResult returns the updated profile
     */
    public BindingResult profileUpdate(Person personFromFrom, String username, BindingResult result, SessionStatus status){
        System.out.println("Transaction ongoing? : "+ TransactionSynchronizationManager.isActualTransactionActive());
        Person personFromDatabase = findPerson(username);
        UpdatePersonValidator updatePersonValidator = new UpdatePersonValidator();

        updatePersonValidator.validate(personFromDatabase, personFromFrom , passwordEncoder,result);
        if(result.hasErrors()) {
            return result;
        }else {
            personRepo.save(personFromDatabase);
            return result;
        }
    }

    /**
     * Get all the competence profiles with the same person id from a person with matching username as the parameter.
     * @param username This is the only parameter of the method getProfileCompetenceMap
     * @return Map returns all the competence profiles with same person id
     */
    public Map<String, Integer> getProfileCompetenceMap(String username) {
        Person person = findPerson(username);
        Iterable<Competence_Profile> competence_Profile = competenceProfileRepo.findAllByPersonId(person.getId());

        CompetenceProfileCompetenceYearDAO test = new CompetenceProfileCompetenceYearDAO();
        Map<String, Integer> map = test.getCompetenceNameAndYear(competence_Profile);

        return map;
    }

    /**
     * This method gets all the competences
     * @return Iterable returns a list of competences
     */
    public Iterable<Competence> getAllCompetence(){
        return competenceRepo.findAll();
    }

    /**
     * This metod is used to delete a competence
     * @param username This is the first parameter of the method deleteCompetenceProfile
     * @param componentName This is the second parameter of the method deleteCompetenceProfile
     */
    public void deleteCompetenceProfile(String username, @PathVariable String componentName){
        Person p = findPerson(username);
        Competence competence = competenceRepo.findByCompetenceName(componentName);
        Competence_Profile profile = competenceProfileRepo.findByPersonAndCompetence(p, competence);
        competenceProfileRepo.delete(profile);
    }

    /**
     * This method is used to add a competence
     * @param httpServletRequest This is the first parameter of the method addCompetenceProfile
     * @param competence This is the second parameter of the method addCompetenceProfile
     */
    public void addCompetenceProfile(HttpServletRequest httpServletRequest, Competence competence){
        String username = httpServletRequest.getUserPrincipal().getName();
        Person p = findPerson(username);

        String year = httpServletRequest.getParameter("year");
        int yearInt = Integer.parseInt(year);
        Competence_Profile checkForDuplicateCompetence = competenceProfileRepo.findByPersonAndCompetence(p,competence);

        if(checkForDuplicateCompetence == null) {
            Competence_Profile newProfile = new Competence_Profile(p, competence, yearInt);
            competenceProfileRepo.save(newProfile);
        }
    }

    /**
     * This method finds the application by person
     * @param person This is the only parameter of the method findApplikationByPerson
     * @return Applikation returns the application linked to the person parameter
     */
    public Applikation findApplikationByPerson(Person person){
        return appRepo.findByPerson(person);
    }

    /**
     * This method adds availability
     * @param httpServletRequest This is the first parameter of the method addAvailability
     * @param availability This is the second parameter of the method addAvailability
     * @param result This is the third parameter of the method addAvailability
     * @return BindingResult returns the result of adding a new availability
     */
    public BindingResult addAvailability(HttpServletRequest httpServletRequest, Availability availability, BindingResult result) {
        String username = httpServletRequest.getUserPrincipal().getName();
        Person person = personRepo.findByUserName(username);
        Availability newAvailability = new Availability(person, availability.getFromDate(), availability.getToDate());

        availabilityValidator.validate(newAvailability,result);
        if(!result.hasErrors())
            availabilityRepo.save(newAvailability);

        return result;
    }

    /**
     * This method deletes availability
     * @param httpServletRequest This is the first parameter of the method deleteAvailability
     * @param id This is the second parameter of the method deleteAvailability
     */
    public void deleteAvailability(HttpServletRequest httpServletRequest, int id){
        String username = httpServletRequest.getUserPrincipal().getName();
        Person person = personRepo.findByUserName(username);
        Availability newAvailability = availabilityRepo.findById(id);

        if(person.getId() == newAvailability.getPerson().getId())
            availabilityRepo.deleteById(id);
    }

    /**
     * This method finds all availability by person id
     * @param id This is the only parameter of the method findAllAvailabilityByPersonId
     * @return Iterable returns all availability with person id that matches the parameter
     */
    public Iterable<Availability> findAllAvailabilityByPersonId(Integer id) {
        return availabilityRepo.findAllByPersonId(id);
    }

    /**
     * This method returns all competence profiles by person id
     * @param id This is the only parameter of the method getAllCompetenceByPersonId
     * @return Iterable returns competence profiles with person id that matches the parameter
     */
    public Iterable<Competence_Profile> getAllCompetenceByPersonId(Integer id) {
        return competenceProfileRepo.findAllByPersonId(id);
    }

    /**
     * This method gets all roles
     * @return Iterable returns all roles
     */
    public Iterable<Role> getAllRoles(){
        return roleRepo.findAll();
    }

    /**
     * This method adds an application
     * @param person This is the only parameter of the method addApplication
     */
    public void addApplication(Person person) {
        Date date = new Date();
        Applikation newApp = new Applikation(person, date);
        appRepo.save(newApp);
    }

    /**
     * This method deletes an application
     * @param httpServletRequest This is the first parameter of the method deleteApplication
     * @param id This is the second parameter of the method deleteApplication
     */
    public void deleteApplication(HttpServletRequest httpServletRequest, int id) {
        String username = httpServletRequest.getUserPrincipal().getName();
        Person person = personRepo.findByUserName(username);
        Applikation applikation = appRepo.findById(id);

        if(person.getId() == applikation.getPerson().getId())
            appRepo.deleteById(id);
    }

    /**
     * This method gets all applications
     * @return ArrayList returns all applications
     */
    public ArrayList<Applikation> getAllApplications() {
        ArrayList<Applikation> applikations = appRepo.findAll();
        return applikations;
    }
}
