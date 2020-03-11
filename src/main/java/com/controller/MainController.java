package com.controller;

import com.DAO.ApplikationDAO;
import com.Error.DatabaseExceptions;
import com.Error.IllegalStateException;
import com.model.*;
import com.service.RecruitmentAppService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Map;

/**
 * This is the MainController class.
 * It controls all communication between the user and the app.
 */
@Controller
@RequestMapping(path="/")
@SessionAttributes("person")
public class MainController {
  private static final Logger LOGGER = LoggerFactory.getLogger(MainController.class);

  @Autowired
  private RecruitmentAppService appService;


  /**
   * Dynamic redirect after user has logged in based on users role.
   * @param authentication
   * @return
   */
  @RequestMapping(path = "/defaultLogginPage")
  public String defaultLogginPage(Authentication authentication){
    UserDetails userDetails = (UserDetails) authentication.getPrincipal();
    for (GrantedAuthority authority :  userDetails.getAuthorities()){
          if(authority.getAuthority().contains("applicant"))
            return "redirect:/application";
          else if(authority.getAuthority().contains("recruiter"))
            return "redirect:/recruiter";
    }
      return "redirect:/index";
  }

  /**
   * This method adds a new person and returns a String that is used to redirect.
   * @param person person that should be added
   * @param result contains error handling
   * @param status
   * @return
   */
  @RequestMapping(path = "/register/add", method = RequestMethod.POST)
    public String addNewPerson(@Valid Person person, BindingResult result, SessionStatus status) throws DatabaseExceptions, IllegalStateException {
    LOGGER.trace("/register/add" + "called by user");
    result=appService.addNewPerson(person,result,status);
    if (result.hasErrors())
      return "register";
    else
      return "redirect:/login?success";
  }


  /**
   * This method adds attribute to the model parameter with a new Person() and returns register.
   * @param model Yhis is the only parameter of the personForm method.
   * @return String this returns register
   */
  @GetMapping("/register")
  public String personForm(Model model) {
    LOGGER.trace("/register called by user");
    model.addAttribute("person", new Person());
    return "register";
  }

  /**
   * This returns the index
   * @return String retuns index
   */
  @RequestMapping("/index")
  String index() {
    LOGGER.trace("/index called by user");
    return "index";
  }

  /**
   * This is used to return to homePage
   * @return String returns index
   */
  @RequestMapping("/")
  public String homePage(){
    LOGGER.trace("/ called by user");
    return "index";
  }

  /**
   * This is used to return login page
   * @return String returns login
   */
  @RequestMapping("/login")
  public String userLogin(){
    LOGGER.trace("/login called by user");
    return "login";
  }

  /**
   * This is used to return user profile
   * @return String returns profile
   */

  @RequestMapping("/profile")
  public String userProfile(HttpServletRequest httpServletRequest, Model model) throws DatabaseExceptions, IllegalStateException {
    Person person = appService.findPerson(httpServletRequest.getUserPrincipal().getName());
    LOGGER.trace("/profile called by user: " + person.getUserName());

    model.addAttribute("person" , person);
    return "profile";
  }

  /**
   * This method updates a users profile with the users requested changes if they are valid.
   * @param updatedPerson The requested changes
   * @param httpServletRequest Current user
   * @param model
   * @param result For tracking validity
   * @param status
   * @return
   */
  @RequestMapping(value = "/profile/profile_update", method = RequestMethod.PUT)
  public String profileUpdate(Person updatedPerson, HttpServletRequest httpServletRequest, Model model,  BindingResult result, SessionStatus status) throws DatabaseExceptions, IllegalStateException {
    String username = httpServletRequest.getUserPrincipal().getName();
    LOGGER.trace("/profile/profile_update" + "called by " + username);
    System.out.println("Transaction ongoing? : "+ TransactionSynchronizationManager.isActualTransactionActive());

      result = appService.profileUpdate(updatedPerson,username,result,status);
    if(result.hasErrors()) {
      LOGGER.trace("/profile/profile_update called by user and number of errors on new data is: " + result.getErrorCount());
      return "profile/profile_update";
    }
    else {
      LOGGER.trace("/profile/profile_update could successfully updated user: " + updatedPerson.getUserName());
      return "profile";
    }
  }

  /**
   * This method is used to get the update profile of a person(user)
   * @param model This is the first parameter of the method profileUpdate2
   * @param httpServletRequest This is the second parameter of the method profileUpdate2
   * @return String This returns the updated profile.
   */
  @RequestMapping(value = "/profile/profile_update", method = RequestMethod.GET)
  public String profileUpdate2(Model model, HttpServletRequest httpServletRequest) throws DatabaseExceptions, IllegalStateException {
    Person person = appService.findPerson(httpServletRequest.getUserPrincipal().getName());
    model.addAttribute("person" , person);
    return "profile/profile_update";
  }

  /**
   * This method is used for logout
   * @return String returns to logout
   */
  @RequestMapping ("/logout-success")
  public String userLogout(HttpServletRequest httpServletRequest){
    LOGGER.trace("Successfully logged out user");
    return "logout";
  }

  /**
   * The method is used to get a person(user) competence profile
   * @param model This is the first parameter of the method profileCompetence
   * @param httpServletRequest This is the first parameter of the method profileCompetence
   * @return String This returns the competence profile.
   */
  @RequestMapping(value = "/profile/profile_competence", method = RequestMethod.GET)
  public String profileCompetence(Model model, HttpServletRequest httpServletRequest) throws DatabaseExceptions, IllegalStateException {
    String username = httpServletRequest.getUserPrincipal().getName();
    LOGGER.trace("/profile/profile_competence called by user: " + username);
    Iterable<Competence> competence = appService.getAllCompetence();
    Map<String, Integer> map = appService.getProfileCompetenceMap(username);

    model.addAttribute("competence" , competence);
    model.addAttribute("map" , map);
    return "/profile/profile_competence";
  }

  /**
   * This method is used when a person(user) deletes a competence
   * @param httpServletRequest This is the first parameter of the method profile_competence_delete
   * @param componentName This is the second parameter of the method profile_competence_delete
   * @return String This returns the competence profile
   */
  @RequestMapping(value = "/profile/profile_competence/delete/{componentName}")
  public String profile_competence_delete(HttpServletRequest httpServletRequest, @PathVariable String componentName) throws DatabaseExceptions, IllegalStateException {
    String username = httpServletRequest.getUserPrincipal().getName();
    LOGGER.trace("/profile/profile_competence/delete/" + componentName + " called by user: " + username);

    appService.deleteCompetenceProfile(username,componentName);
    return "redirect:/profile/profile_competence";
  }

  /**
   * This method is used when the person(user) wants to add a competence to their profile
   * @param httpServletRequest This is the first parameter of the method profile_competence_add
   * @param competence This is the first parameter of the method profile_competence_add
   * @return String This returns the competence profile
   */
  @RequestMapping(value = "/profile/profile_competence/add", method = RequestMethod.POST)
  public String profile_competence_add(HttpServletRequest httpServletRequest, Competence competence) throws DatabaseExceptions, IllegalStateException {
    LOGGER.trace("/profile/profile_competence/add called by user: " + httpServletRequest.getUserPrincipal().getName());
    appService.addCompetenceProfile(httpServletRequest,competence);
    return "redirect:/profile/profile_competence";
  }

  /**
   * This method fetches all data needed for the rendering of the
   * "Application" page and redirects the user there.
   * @param httpServletRequest with relevant authorization information
   * @param model of applikationDAO object
   * @return .html that should be loaded
   */
  @RequestMapping(value = "/application", method = RequestMethod.GET)
  public String application(HttpServletRequest httpServletRequest, Model model) throws DatabaseExceptions, IllegalStateException {
    Person person = appService.findPerson(httpServletRequest.getUserPrincipal().getName());
    LOGGER.trace("/application (get) called by user: " + person.getUserName());
    Applikation applikation = appService.findApplikationByPerson(person);

    Iterable <Availability> availability = appService.findAllAvailabilityByPersonId(person.getId());
    Iterable<Competence_Profile> competence_Profile = appService.getAllCompetenceByPersonId(person.getId());

    ApplikationDAO applikationDAO = new ApplikationDAO(applikation, availability,competence_Profile);
    model.addAttribute("applikationDAO" , applikationDAO);
    return "/application/application";
  }

  /**
   * For when a user makes a post request with new availability dates.
   * @param httpServletRequest with relevant authorization information
   * @param availability availability object with from-to dates.
   * @param result result of validation
   * @return .html that should be loaded
   */
  @RequestMapping(value = "/application", method = RequestMethod.POST)
  public String applicationAddAvailability(HttpServletRequest httpServletRequest, Availability availability, BindingResult result) throws DatabaseExceptions, IllegalStateException {
    LOGGER.trace("/application (post) called by user: " + httpServletRequest.getUserPrincipal().getName());
    result=appService.addAvailability(httpServletRequest, availability,result);

    if (result.hasErrors()) {
      LOGGER.trace("/application (post) - User tried to addAvailability but failed with: " + result.getErrorCount() + " errors");
      return "redirect:/application?badDates";
    }
    else {
      LOGGER.trace("/application (post) - Successfully added new Availability");
      return "redirect:/application?add";
    }
  }

  /**
   * Deletes dates that an applicant (user) can't work
   * @param httpServletRequest with relevant authorization information
   * @param id of Availability-object that should be removed
   * @return .html that should be loaded
   */
  @RequestMapping(value = "/application/deleteAvailability/{id}", method = RequestMethod.GET)
  public String applicationDeleteAvailability(HttpServletRequest httpServletRequest, @PathVariable("id") int id) throws IllegalStateException, DatabaseExceptions {
    LOGGER.trace("/application/deleteAvailability/" + id + " was called by user: " + httpServletRequest.getUserPrincipal().getName());
    appService.deleteAvailability(httpServletRequest,id);
    return"redirect:/application?deleteAvailability";
  }

  /**
   * Enables the user to create an application.
   * @param httpServletRequest This is the only parameter of the method applicationAddApplication
   * @return String .html that should be loaded
   */
  @RequestMapping(value = "/application/addApplication", method = RequestMethod.GET)
  public String applicationAddApplication(HttpServletRequest httpServletRequest) throws DatabaseExceptions, IllegalStateException {
    Person person = appService.findPerson(httpServletRequest.getUserPrincipal().getName());
    appService.addApplication(person);

    return "redirect:/application?add";
  }

  //TODO FIX

  /**
   * Deletes an Application from the users profile.
   * @param httpServletRequest This is the first parameter of the method applicationDeleteApplication
   * @param id This is the second parameter of the method applicationDeleteApplication
   * @return  .html that should be loaded
   */
  @RequestMapping(value = "/application/deleteApplication/{id}", method = RequestMethod.GET)
  public String applicationDeleteApplication(HttpServletRequest httpServletRequest, @PathVariable("id") int id) throws IllegalStateException, DatabaseExceptions {
    appService.deleteApplication(httpServletRequest, id);

    return "redirect:/application?deleteApplication";
  }

  /**
   * Main page for Recruiter, lists all the Applications and supports filter.
   * @param model contains objects that may be showed in view-layer.
   * @return .html that should be loaded
   */
  @RequestMapping(value ="/recruiter")
  public String recruiter(Model model) throws DatabaseExceptions {
    ArrayList<Applikation> applikations =  appService.getAllApplications();
    ArrayList<Status> status = appService.getAllStatus();

    model.addAttribute("status", status);
    model.addAttribute("applikations", applikations);

    return "/recruiter/recruiter";
  }
  //TODO FIX

  /**
   * Take cares of a filter search from /recruiter page.
   * @param model contains objects that may be showed in view-layer.
   * @param availability which dates Recruiter wants to filter on
   * @param statusFilter whicch status Recruiter wants to filter on
   * @return .html that should be loaded
   */
  @RequestMapping(value ="/recruiter", method = RequestMethod.POST)
  public String recruiterFilter(Model model, Availability availability, Status statusFilter, HttpServletRequest httpServletRequest) throws DatabaseExceptions {
    LOGGER.trace("/recruiter (post) was called by user: " + httpServletRequest.getUserPrincipal().getName());
    ArrayList<Applikation> applikations = appService.getAllApplicationsByFilter(availability, statusFilter);
    ArrayList<Status> status = appService.getAllStatus();

    model.addAttribute("status", status);
    model.addAttribute("applikations", applikations);

    return "/recruiter/recruiter";
  }

  /**
   * Let recruiter view a specific application and be able to change status on it.
   * @param model contains objects that may be showed in view-layer.
   * @param id on specific application to be viewed
   * @return .html that should be loaded
   */
  @RequestMapping(value = "/recruiter/manage_application/{id}", method = RequestMethod.GET)
  public String recruiterManage_applications(Model model, @PathVariable("id") int id) throws DatabaseExceptions, IllegalStateException {
    Person person = appService.findPersonByApplikationId(id);
    LOGGER.trace("/recruiter/manage_application/" + id + " was called by user: " + person.getUserName());

    Applikation application = appService.getApplicationById(id);
    ArrayList<Status> status = appService.getAllStatus();
    ArrayList<Availability> availabilities = appService.getAvailabilitiesByPersonOrderByAsc(person);
    ArrayList<Competence_Profile> Competence_Profiles = appService.findAllByPersonOrderByCompetence(person);

    model.addAttribute("competence_profiles", Competence_Profiles);
    model.addAttribute("availability", availabilities);
    model.addAttribute("status", status);
    model.addAttribute("applikation", application);

    return "recruiter/manage_application";
  }

  /**
   * Lets recruiter change status on a specific application
   * @param status new status on application
   * @param id on application that should be changed
   * @return .html that should be loaded
   */
  @RequestMapping(value = "/recruiter/manage_application/{id}", method = RequestMethod.POST)
  public String recruiterUpdateApplicationStatus(HttpServletRequest httpServletRequest, Status status, @PathVariable("id") int id) throws DatabaseExceptions, IllegalStateException {
    LOGGER.trace("/recruiter/manage_application/" + id + " was called by user: " + httpServletRequest.getUserPrincipal().getName());
    appService.applicationUpdateStatus(id, status.getStatusId());

    return "redirect:/recruiter/manage_application/" + id;
  }
}
