package com.service;

import com.DAO.competenceProfileCompetenceYearDAO;
import com.model.*;
import com.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.support.SessionStatus;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import javax.validation.Valid;
import java.util.Map;

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
    public Iterable<Person> getAllPeople(){
        return personRepo.findAll();
    }
    public Person findPerson(String userName){
        return personRepo.findByUserName(userName);
    }
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
    public Map<String, Integer> getProfileCompetenceMap(String username) {
        Person person = findPerson(username);
        Iterable<Competence> competence = getAllCompetence();
        Iterable<Competence_Profile> competence_Profile = competenceProfileRepo.findAllByPersonId(person.getId());

        competenceProfileCompetenceYearDAO test = new competenceProfileCompetenceYearDAO();
        Map<String, Integer> map = test.getCompetenceNameAndYear(person.getId(),competence_Profile, competence);

        return map;
    }
    public Iterable<Competence> getAllCompetence(){
        return competenceRepo.findAll();
    }
    public void deleteCompetenceProfile(String username, @PathVariable String componentName){
        Person p = findPerson(username);
        Competence competence = competenceRepo.findByCompetenceName(componentName);
        Competence_Profile profile = competenceProfileRepo.findByPersonAndCompetence(p, competence);
        competenceProfileRepo.delete(profile);
    }
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
    public Applikation findApplikationByPerson(Person person){
        return appRepo.findByPerson(person);
    }


    public BindingResult addAvailability(HttpServletRequest httpServletRequest, Availability availability, BindingResult result) {
        String username = httpServletRequest.getUserPrincipal().getName();
        Person person = personRepo.findByUserName(username);
        Availability newAvailability = new Availability(person, availability.getFromDate(), availability.getToDate());

        availabilityValidator.validate(newAvailability,result);
        if(!result.hasErrors())
            availabilityRepo.save(newAvailability);

        return result;
    }

    public void deleteAvailability(HttpServletRequest httpServletRequest, int id){
        String username = httpServletRequest.getUserPrincipal().getName();
        Person person = personRepo.findByUserName(username);
        Availability newAvailability = availabilityRepo.findById(id);

        if(person.getId() == newAvailability.getPerson().getId())
            availabilityRepo.deleteById(id);
    }

    public Iterable<Availability> findAllAvaiilabilityByPersonId(Integer id) {
        return availabilityRepo.findAllByPersonId(id);
    }

    public Iterable<Competence_Profile> getAllCompetenceByPersonId(Integer id) {
        return competenceProfileRepo.findAllByPersonId(id);
    }
}
