package com.service;

import com.DAO.CompetenceProfileCompetenceYearDAO;
import com.Error.DatabaseExceptions;
import com.Error.IllegalStateException;
import com.controller.MainController;
import com.model.*;
import com.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.support.SessionStatus;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.xml.crypto.Data;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

@Transactional
@Service
public class RecruitmentAppService {
    private static final Logger LOGGER = LoggerFactory.getLogger(MainController.class);
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

    private String INTERNAL_SERVER_ERROR = "Internal Server Error";


    public BindingResult addNewPerson(@Valid Person person, BindingResult result, SessionStatus status) throws DatabaseExceptions, IllegalStateException {
        String errMsgToUser = "Error while saving new person, please try again";
        try{
            personValidator.validate(person, result);
            if(result.hasErrors()){
                LOGGER.trace("addNewPerson - could not add user. Total number of errors is: " +  result.getErrorCount());
                return result;
            }else {
                String encodedPassword = passwordEncoder.encode(person.getPassword());
                person.setPassword(encodedPassword);
                person.setRoleId(2);
                personRepo.save(person);
                status.setComplete();
                LOGGER.trace("addNewPerson - successfully added new user " +  person.getUserName());
                return result;
            }
        }
        catch (DataAccessException dataAccessException){
            LOGGER.error("addNewPerson - DataAccessException. COULD NOT SAVE USER TO DATABASE");
            throw new DatabaseExceptions(errMsgToUser);
        }
    }

    public Iterable<Person> getAllPeople() throws DatabaseExceptions, IllegalStateException {
        try {
            return personRepo.findAll();
        }
        catch (DataAccessException dataAccessException){
            LOGGER.error("getAllPeople - COULD NOT SAVE USER TO DATABASE. " + dataAccessException.getMessage());
            throw new DatabaseExceptions(INTERNAL_SERVER_ERROR);
        }
    }


    public Person findPerson(String userName) throws DatabaseExceptions, IllegalStateException {
        try {
            Person person = personRepo.findByUserName(userName);
            if(person == null) {
                LOGGER.warn("findPerson, COULD NOT FIND PERSON " + userName + " FROM DATABASE");
                throw new IllegalStateException("COULD NOT FIND PERSON: " + userName + " FROM DATABASE");
            }
            return person;
        }
        catch (DataAccessException dataAccessException){
            LOGGER.warn("findPerson - DataAccessException on user: " + userName + "." + dataAccessException.getMessage());
            throw new DatabaseExceptions(INTERNAL_SERVER_ERROR);
        }
    }

    public Person findPersonBySsn(String ssn) throws DatabaseExceptions, IllegalStateException {
        String errMsgToUser = "Internal server error, could not find person";
        try{
            Person person = personRepo.findBySsn(ssn);

            if(person == null) {
                LOGGER.warn("findPerson, COULD NOT FIND PERSON BY SSN: " + ssn + " FROM DATABASE");
                throw new IllegalStateException("COULD NOT FIND PERSO BY SSN: " + ssn + " FROM DATABASE");
            }
            return person;
        }

        catch (DataAccessException dataAccessException){
            LOGGER.warn("findPersonBySsn - COULD NOT FIND USER WITH SSN: " + ssn + ". DATABASE ERROR. " + dataAccessException.getMessage());
            throw new DatabaseExceptions(INTERNAL_SERVER_ERROR);
        }
    }

    // REMOVE??
    public Person findPersonByEmail(String email) throws DatabaseExceptions {
        String errMsgToUser = "Internal server error - Could not find user";
        try {
            return personRepo.findByEmail(email);
        }
        catch (NullPointerException nullpointerException){
            LOGGER.error("findPersonByEmail - COULD NOT FIND USER WITH EMAIL: " + email + ". " + nullpointerException.getMessage());
            throw new DatabaseExceptions(errMsgToUser);
        }
        catch (DataAccessException dataAccessException){
            LOGGER.error("findPersonByEmail - COULD NOT FIND USER WITH EMAIL: " + email + ". ERROR IN DATABASE" + dataAccessException.getMessage());
            throw new DatabaseExceptions(errMsgToUser);
        }
        catch (Exception e){
            LOGGER.error("findPersonByEmail - COULD NOT FIND USER WITH EMAIL: " + email + ". GENERIC EXCEPTION" + e.getMessage());
            throw new DatabaseExceptions(errMsgToUser);
        }
    }

    /**
     *
     * @param personFromFrom Person object with new information
     * @param username username of the person to be updated
     * @param result contains error messages
     * @param status
     * @return BindingResult with error messages if it exists any.
     * @throws DatabaseExceptions
     */
    public BindingResult profileUpdate(Person personFromFrom, String username, BindingResult result, SessionStatus status) throws DatabaseExceptions, IllegalStateException {
        Person personFromDatabase;

        System.out.println("Transaction ongoing? : "+ TransactionSynchronizationManager.isActualTransactionActive());
        try {
            UpdatePersonValidator updatePersonValidator = new UpdatePersonValidator();
            personFromDatabase = findPerson(username);
            updatePersonValidator.validate(personFromDatabase, personFromFrom , passwordEncoder,result);
            if(!result.hasErrors()) {
                personRepo.save(personFromDatabase);
            }
            return result;
        }
        catch (DataAccessException dataAccessException){
            LOGGER.error("profileUpdate - COULD NOT UPDATE USER: " + username  + ". DATABASE ERROR. " + dataAccessException.getMessage());
            throw new DatabaseExceptions(INTERNAL_SERVER_ERROR);
        }


    }


    public Map<String, Integer> getProfileCompetenceMap(String username) throws DatabaseExceptions, IllegalStateException {
            Person person = findPerson(username);
            Iterable<Competence_Profile> competence_Profile = competenceProfileRepo.findAllByPersonId(person.getId());

            CompetenceProfileCompetenceYearDAO test = new CompetenceProfileCompetenceYearDAO();
            Map<String, Integer> map = test.getCompetenceNameAndYear(competence_Profile);
            return map;
    }


    public Iterable<Competence> getAllCompetence() throws DatabaseExceptions {
        try {
            return competenceRepo.findAll();
        }
        catch(DataAccessException dataAccessException){
            LOGGER.error("DataAccessException IN getAllCompetence() " + dataAccessException.getMessage());
            throw new DatabaseExceptions(INTERNAL_SERVER_ERROR);
        }
    }


    public void deleteCompetenceProfile(String username, @PathVariable String componentName) throws DatabaseExceptions, IllegalStateException {
        try {
            Person p = findPerson(username);
            Competence competence = competenceRepo.findByCompetenceName(componentName);

            if(competence == null){
                LOGGER.error("COULD NOT FIND COMPETENCE: " + componentName + " IN DATABASE");
                throw new IllegalStateException("YOU ARE TRYING TO DELETE AN COMPETENCE THAT DOES NOT EXISTS");
            }

            Competence_Profile profile = competenceProfileRepo.findByPersonAndCompetence(p, competence);

            if(profile == null){
                LOGGER.error("COULD NOT FIND COMPETENCE_PROFILE WITH PERSON ID: " + p.getId() + "AND COMPETENCE ID " + competence.getCompetenceId() + " IN DATABASE");
                throw new IllegalStateException("COULD NOT FIND A MATCHING PERSON AND COMPETENCE");
            }

            competenceProfileRepo.delete(profile);
        }
        catch(DataAccessException dataAcccess) {
            LOGGER.error("COULD NOT DELETE COMPETENCE_PROFILE WITH USERNAME " + username + " AND COMPETENCE NAME:  " + componentName + " FROM DATABASE. " + dataAcccess.getMessage());
            throw new DatabaseExceptions(INTERNAL_SERVER_ERROR);
        }
    }

    public void addCompetenceProfile(HttpServletRequest httpServletRequest, Competence competence) throws DatabaseExceptions, IllegalStateException {
        try{
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
        catch(DataAccessException dataAcccess) {
            LOGGER.error("addCompetenceProfile - DataAccessException: " + dataAcccess.toString());
            throw new DatabaseExceptions(INTERNAL_SERVER_ERROR);
        }
        catch(NumberFormatException numberFormatException){
            LOGGER.warn("USER TRIES TO ADD YEAR WITH BAD VALUE. CANT TRAANSFROM string - > int in addCompetenceProfile(). " + numberFormatException.getMessage());
            throw new IllegalStateException("BAD VALUE ON YEAR");
        }

    }


    public Applikation findApplikationByPerson(Person person) throws DatabaseExceptions, IllegalStateException {
        try{
            Applikation app = appRepo.findByPerson(person);
            return app;
        }
        catch(DataAccessException dataAccessException){
            throw new DatabaseExceptions(INTERNAL_SERVER_ERROR);
        }
        catch(NullPointerException nullPointerException){
            LOGGER.warn("COULD NOT 0TO FIND APPLICATION BY PERSON: " + person.getUserName() + " IN findApplikationByPerson().");
            throw new IllegalStateException("COULD NOT FIND APPLICATION BY PERSON : " + person.getUserName());
        }
    }


    public BindingResult addAvailability(HttpServletRequest httpServletRequest, Availability availability, BindingResult result) throws DatabaseExceptions, IllegalStateException {
        try {
            String username = httpServletRequest.getUserPrincipal().getName();
            Person person = findPerson(username);
            Availability newAvailability = new Availability(person, availability.getFromDate(), availability.getToDate());

            availabilityValidator.validate(newAvailability, result);
            if (!result.hasErrors())
                availabilityRepo.save(newAvailability);

            return result;
        }
        catch(DataAccessException dataAcccess) {
            LOGGER.error("addAvailability - DataAccessException: " + dataAcccess.toString());
            throw new DatabaseExceptions(INTERNAL_SERVER_ERROR);
        }
    }

    public void deleteAvailability(HttpServletRequest httpServletRequest, int id) throws IllegalStateException, DatabaseExceptions {
        try {
            String username = httpServletRequest.getUserPrincipal().getName();
            Person person = personRepo.findByUserName(username);
            Availability newAvailability = availabilityRepo.findById(id);

            if (person == null){
                LOGGER.warn("COULD NOT FIND PERSON THAT SENT REQUEST TO deleteAvailability IN DATABASE");
                throw new IllegalStateException("COULD NOT FIND PERSON FROM DATABASE");
            }

            if (newAvailability == null) {
                LOGGER.warn("COULD NOT FIND AVAILABILITY WITH ID: " + id + " IN DATABASE. ");
                throw new IllegalStateException("COULD NOT FIND AVAILABILITY FROM DATABASE");
            }

            if (person.getId() == newAvailability.getPerson().getId())
                availabilityRepo.deleteById(id);
            else {
                LOGGER.warn("PERSON ID AND AVAILABILITY.PERSON.GET() DONT MATCH");
                throw new IllegalStateException("PERSON AND newAvailability.getPerson().getId() DONT MATCH");
            }
        }
        catch(DataAccessException dataAccessException){
            throw new DatabaseExceptions("DATABASE EXCEPTION IN deleteAvailability. " + dataAccessException.getMessage());
        }
    }


    public Iterable<Availability> findAllAvailabilityByPersonId(Integer id) throws DatabaseExceptions {
        try {
            return availabilityRepo.findAllByPersonId(id);
        }
        catch (DataAccessException dataAccessException){
            LOGGER.error("DataAccessException WHILE GETTING availabilityRepo.findAllByPersonId() IN findAllAvailabilityByPersonId(Integer id))");
            throw new DatabaseExceptions(INTERNAL_SERVER_ERROR);
        }
    }


    public Iterable<Competence_Profile> getAllCompetenceByPersonId(Integer id) throws DatabaseExceptions {
        try {
            return competenceProfileRepo.findAllByPersonId(id);
        }
        catch(DataAccessException dataAccessException){
            LOGGER.error("DataAccessException WHILE GETTING CompetenceProfileRepo.findAllByPersonId(id) WITH ID: " + id + " IN getAllCompetenceByPersonId(Integer id)");
            throw new DatabaseExceptions("COULD NOT FIND COMPETENCES FROM PERSON");
        }
    }


    public Iterable<Role> getAllRoles() throws DatabaseExceptions {
        try {
            return roleRepo.findAll();
        }
        catch(DataAccessException dataAccessException){
            LOGGER.error("DataAccessException IN getAllRoles()");
            throw new DatabaseExceptions(INTERNAL_SERVER_ERROR);
        }
    }


    public void addApplication(Person person) throws DatabaseExceptions, IllegalStateException {
        if(person == null) {
            LOGGER.warn("USER TRIES TO ADD APPLICATION WITH PERSON OBJECT IS NULL");
            throw new IllegalStateException("COULD NOT ADD APPLICATION TO PERSON");
        }

        try {
            Date date = new Date();
            Applikation newApp = new Applikation(person, date);
            appRepo.save(newApp);
        }
        catch(DataAccessException dataAccessException){
            LOGGER.error("DataAccessException IN addApplication()");
            throw new DatabaseExceptions(INTERNAL_SERVER_ERROR);
        }
    }


    public void deleteApplication(HttpServletRequest httpServletRequest, int id) throws IllegalStateException, DatabaseExceptions {
        String username = httpServletRequest.getUserPrincipal().getName();
        Person person = findPerson(username);
        Applikation applikation = appRepo.findApplikationByApplikationId(id);

        try{
            if (person.getId() == applikation.getPerson().getId())
                appRepo.deleteById(id);
            else {
                LOGGER.warn("PERSON TRIES TO DELETE APPLICATION THAT IS NOT ASSOCIATED WITH THAT PERSON");
            }
        }
        catch(DataAccessException dataAccessException){
            LOGGER.error("DataAccessException IN deleteApplication() with id: " + id);
            throw new DatabaseExceptions(INTERNAL_SERVER_ERROR);
        }
    }


    public ArrayList<Applikation> getAllApplications() throws DatabaseExceptions {
        try{
            ArrayList<Applikation> applikations = appRepo.findAll();
            return applikations;
        }
        catch(DataAccessException dataAccessException){
            LOGGER.error("DataAccessException IN getAllApplications()");
            throw new DatabaseExceptions(INTERNAL_SERVER_ERROR);
        }
    }


    public Applikation getApplicationById(int id) throws DatabaseExceptions {
        try{
            return appRepo.getOne(id);
        }
        catch(DataAccessException dataAccessException){
            LOGGER.error("DataAccessException IN getApplicationById() with id: " + id + ". " + dataAccessException.getMessage());
            throw new DatabaseExceptions(INTERNAL_SERVER_ERROR);
        }
    }


    public ArrayList<Status> getAllStatus() throws DatabaseExceptions {
        try{
            return statusRepo.findAll();
        }

         catch(DataAccessException dataAccessException){
            LOGGER.error("DataAccessException IN getAllStatus()");
            throw new DatabaseExceptions(INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Updated status on a specific application
     * @param applicationId id on the application that should be updated
     * @param statusId new status
     */
    public void applicationUpdateStatus(int applicationId, Integer statusId) throws DatabaseExceptions, IllegalStateException {
        try {
            Applikation application = appRepo.findApplikationByApplikationId(applicationId);

            Status status = statusRepo.getOne(statusId);
            if(application == null){
                LOGGER.warn("nullPointerException ON EITHER APLLICATION WITH ID: " + applicationId + " OR STATUS WITH ID " + statusId +" in applicationUpdateStatus. ");
                throw new IllegalStateException("COULD NOT FIND YOUR APPLICATION");
            }
            if(status == null) {
                LOGGER.warn("COULD NOT FIND status WITH ID: " + statusId + " in applicationUpdateStatus. ");
                throw new IllegalStateException("COULD NOT FIND YOUR STATUS");
            }
                application.setStatus(status);
                appRepo.save(application);
            }

        catch(DataAccessException dataAccessException){
            LOGGER.error("DataAccessException IN applicationUpdateStatus() with applicationID: " + applicationId + "and status Id: " + statusId + ". " + dataAccessException.getMessage());
            throw new DatabaseExceptions(INTERNAL_SERVER_ERROR);
        }

    }


    public ArrayList<Availability> getAvailabilitiesByPersonOrderByAsc(Person person) throws DatabaseExceptions, IllegalStateException {
        if(person == null){
            LOGGER.warn("TRYING TO GET AVAILABILITES FROM A NULL PERSON IN getAvailabilitiesByPersonOrderByAsc");
            throw new IllegalStateException("FAILED TO GET PERSON");
        }

        try {
            return availabilityRepo.getAvailabilitiesByPersonOrderByFromDate(person);
        }

        catch(DataAccessException dataAccessException){
            LOGGER.warn("DataAccessException in getAvailabilitiesByPersonOrderByAsc with Personid: " + person.getId());
            throw new DatabaseExceptions(INTERNAL_SERVER_ERROR);
        }
    }


    public ArrayList<Competence_Profile> findAllByPersonOrderByCompetence(Person person) throws IllegalStateException, DatabaseExceptions {
        if(person == null){
            LOGGER.warn("TRYING TO GET Competence_Profile FROM A NULL PERSON IN findAllByPersonOrderByCompetence");
            throw new IllegalStateException("FAILED TO GET PERSON");
        }
        try {
            return competenceProfileRepo.findAllByPersonOrderByCompetence(person);
        }
        catch(DataAccessException dataAccessException){
            LOGGER.warn("DataAccessException in getAvailabilitiesByPersonOrderByAsc with Personid: " + person.getId());
            throw new DatabaseExceptions(INTERNAL_SERVER_ERROR);
        }
    }

    //TODO NEED ERROR HANDLING WHEN IT IS DONE
    public ArrayList<Applikation> getAllApplicationsByFilter(Availability availability, Status statusFilter) throws DatabaseExceptions {
        ArrayList<Applikation> applikations = new ArrayList<>();
        DateFormat targetFormat = new SimpleDateFormat("yyyy-MM-dd");
        String toDate = "";
        String fromDate = "";
        ArrayList<Integer> personIds;

        //Fix right pattern for DB search, dates is null => return all applications
        if(availability.getFromDate() != null && availability.getToDate() != null) {
            fromDate = targetFormat.format(availability.getFromDate());
            toDate = targetFormat.format(availability.getToDate());
            personIds = availabilityRepo.getAllPersonIdsFromDates(fromDate, toDate);
        }
        else
            //dates is null, return all applications
            //return getAllApplications();
            personIds = availabilityRepo.getAllPersonId();

        for(Integer personId : personIds){
            Applikation getApp = (appRepo.findByPersonId(personId));
            //Goes to next application, if getApp is null aka Applicant has send in availability-dates but not sent in application.
            if(getApp == null)
                continue;


            //Status filter null aka Recruiter filter on all status.
            if(statusFilter.getStatusId() == null || getApp.getStatus().getStatusId() == statusFilter.getStatusId())
                applikations.add(getApp);
        }

        for (Applikation asd : applikations ) {
            System.out.println(asd.getApplikationId());
        }
            return applikations;
    }


    public Person findPersonById(int id) throws IllegalStateException, DatabaseExceptions {
        try {
            Person person = personRepo.findById(id);
            return person;
        }
        catch(DataAccessException dataAccessException){
            LOGGER.warn("DataAccessException in findPersonById with Personid: " + id + ". " + dataAccessException.getMessage());
            throw new DatabaseExceptions(INTERNAL_SERVER_ERROR);
        }
        catch(NullPointerException nullPointerException){
            LOGGER.warn("TRYING TO GET A NON EXISTING PERSON WITH ID: " + id + " in findPersonById.");
            throw new IllegalStateException("FAILED TO GET PERSON");
        }
    }


    public Person findPersonByApplikationId(int id) throws DatabaseExceptions, IllegalStateException {
        try{
            Person person = appRepo.findApplikationByApplikationId(id).getPerson();
            return person;
        }
        catch(DataAccessException dataAccessException){
            LOGGER.warn("DataAccessException in findPersonByApplikationId with Application id: " + id + ". " + dataAccessException.getMessage());
            throw new DatabaseExceptions(INTERNAL_SERVER_ERROR);
        }
        catch(NullPointerException nullPointerException){
            LOGGER.warn("COULD NOT FIND AN PERSON THAT WAS ASSOCIATED WITH APLLICATION ID: " + id + " in findPersonByApplikationId.");
            throw new IllegalStateException("FAILED TO GET PERSON");
        }
    }

    public boolean personExistsByUsername(String username){
        Person person = personRepo.findByUserName(username);

        if(person == null)
            return false;
        else
            return true;
    }

    public boolean personExistsBySSN(String ssn) {
        Person person = personRepo.findBySsn(ssn);

        if(person == null)
            return false;
        else
            return true;
    }
}
