package com.controller;

import com.DAO.competenceProfileCompetenceYearDAO;
import com.model.Applikation;
import com.model.Competence;
import com.model.Competence_Profile;
import com.model.Person;
import com.repository.ApplikationRepository;
import com.repository.CompetenceProfileRepository;
import com.repository.CompetenceRepository;
import com.repository.PersonRepository;
import com.service.RecruitmentAppService;
import com.service.UpdatePersonValidator;
import com.service.PersonValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import com.service.PersonValidator;
import org.springframework.web.bind.support.SessionStatus;
import javax.servlet.http.HttpServletRequest;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.*;

/**
 * This is the MainController class.
 * It controls the addition of a person and the changes of a web page.
 */
@Controller
@RequestMapping(path="/")
@SessionAttributes("person")
public class MainController {

  @Autowired
  private PersonRepository personRepository;
  @Autowired
  private PersonValidator personValidator;
  @Autowired
  private CompetenceRepository competenceRepository;
  @Autowired
  private CompetenceProfileRepository competenceProfileRepository;
  @Autowired
  private PasswordEncoder passwordEncoder;
  @Autowired
  private ApplikationRepository applikationRepository;
  @Autowired
  private RecruitmentAppService appService;
  /**
   * This method adds a new person and returns a String that is used to redirect.
   * @param person person that should be added
   * @param result contains error handling
   * @param status
   * @return
   */
  @RequestMapping(path = "/register/add", method = RequestMethod.POST)
    public String addNewPerson(@Valid Person person, BindingResult result, SessionStatus status){
    result=appService.addNewPerson(person,result,status);
    if (result.hasErrors()) {
      return "register";
    } else {
      return "redirect:/login?success";
    }
  }

  /**
   * This method is used to get all the people from the repository and add their attributes to the model parameter.
   * @param model This is the only parameter of the getAllPeople method.
   * @return String this returns report1testresult
   */
  /*@RequestMapping(value = "success/{addsuccess}", method = RequestMethod.GET)
  public String success(@PathVariable("addsuccess") String addsuccess, Model model)
  {
    model.addAttribute("addsuccess",addsuccess);
    return "redirect:/login?success";
  }*/

  @GetMapping(path="/report1testresult")
  public String getAllPeople(Model model){
      Iterable<Person> people = appService.getAllPeople();
      model.addAttribute("people",people);
    return "report1testresult";
  }

  /**
   * This method adds attribute to the model parameter with a new Person() and returns register.
   * @param model Yhis is the only parameter of the personForm method.
   * @return String this returns register
   */
  @GetMapping("/register")
  public String personForm(Model model) {
    model.addAttribute("person", new Person());
    return "register";
  }

  /**
   * This method is used to submit the attribute person
   * @param person This is the only parameter of the method personSubmit
   * @return String returns register
   */
  /*@PostMapping("/register")
  public String personSubmit(@ModelAttribute Person person) {
    return "register";
  }*/

  /**
   * This returns the index
   * @return String retuns index
   */
  @RequestMapping("/index")
  String index() {
    return "index";
  }

  /**
   * This is used to test the report
   * @return String returns report1test
   */
  @RequestMapping("/report")
  String repTest() {
    return "report1test";
  }

  /**
   * This is used to return to homePage
   * @return String returns index
   */
  @RequestMapping("/")
  public String homePage(){
    return "index";
  }

  /**
   * This is used to return login page
   * @return String returns login
   */
  @RequestMapping("/login")
  public String userLogin(){

    
    return "login";
  }

  /**
   * This is used to return user profile
   * @return String returns profile
   */

  @RequestMapping("/profile")
  public String userProfile(HttpServletRequest httpServletRequest, Model model){
    Person person = appService.findPerson(httpServletRequest.getUserPrincipal().getName());
    model.addAttribute("person" , person);
    return "profile";
  }

  /**
   * This is used to return user logout.
   * @return String returns logout
   */
  @RequestMapping(value = "/profile/profile_update", method = RequestMethod.PUT)
  public String profileUpdate(Person personFromFrom, HttpServletRequest httpServletRequest, Model model,  BindingResult result, SessionStatus status){
    System.out.println("Transaction ongoing? : "+ TransactionSynchronizationManager.isActualTransactionActive());
    result = appService.profileUpdate(personFromFrom,httpServletRequest.getUserPrincipal().getName(),result,status);
    if(result.hasErrors()) {
      return "profile/profile_update";
    }else {
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
  public String profileUpdate2(Model model, HttpServletRequest httpServletRequest){
    Person person = appService.findPerson(httpServletRequest.getUserPrincipal().getName());
    model.addAttribute("person" , person);
    return "profile/profile_update";
  }

  /**
   * This method is used for logout
   * @return String returns to logout
   */
  @RequestMapping ("/logout-success")
  public String userLogout(){
    return "logout";
  }

  /**
   * This is used to return locked Page
   * @return String returns lockedpage
   */
  @RequestMapping("/lockedpage")
  public String lockedPage(){
    return "lockedpage";
  }

  /**
   * The method is used to get a person(user) competence profile
   * @param model This is the first parameter of the method profileCompetence
   * @param httpServletRequest This is the first parameter of the method profileCompetence
   * @return String This returns the competence profile.
   */
  @RequestMapping(value = "/profile/profile_competence", method = RequestMethod.GET)
  public String profileCompetence(Model model, HttpServletRequest httpServletRequest) {
    Iterable<Competence> competence = appService.getAllCompetence();
    Map<String, Integer> map = appService.getProfileCompetenceMap(httpServletRequest.getUserPrincipal().getName());

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
  public String profile_competence_delete(HttpServletRequest httpServletRequest, @PathVariable String componentName){
    appService.deleteCompetenceProfile(httpServletRequest.getUserPrincipal().getName(),componentName);
    return "redirect:/profile/profile_competence";
  }
/*
  @RequestMapping(value = "/profile/profile_competence/add/{componentName}/{year}")
  public String profile_competence_add(HttpServletRequest httpServletRequest, @PathVariable("componentName") String componentName, @PathVariable("year") String year){
    String username = httpServletRequest.getUserPrincipal().getName();
    Person p = personRepository.findByUserName(username);

    System.out.println(p.getId());
    Competence competence = competenceRepository.findByCompetenceName(componentName);
    int yearInt = Integer.parseInt(year);

    Competence_Profile asd = competenceProfileRepository.findByPersonAndCompetence(p,competence);

    if(asd == null) {
      Competence_Profile newProfile = new Competence_Profile(p, competence, yearInt);
      competenceProfileRepository.save(newProfile);
    }
    //Competence_Profile profile = competenceProfileRepository.findByPersonAndCompetence(p, competence);
    //competenceProfileRepository.delete(profile);
    return "redirect:/";
  }
*/

  /**
   * This method is used when the person(user) wants to add a competence to their profile
   * @param httpServletRequest This is the first parameter of the method profile_competence_add
   * @param competence This is the first parameter of the method profile_competence_add
   * @return String This returns the competence profile
   */
  @RequestMapping(value = "/profile/profile_competence/add", method = RequestMethod.POST)
  public String profile_competence_add(HttpServletRequest httpServletRequest, Competence competence){
    appService.addCompetenceProfile(httpServletRequest,competence);
    return "redirect:/profile/profile_competence";
  }

  @RequestMapping(value = "/application/application", method = RequestMethod.GET)
  public String application(HttpServletRequest httpServletRequest, Model model){
    Person person = appService.findPerson(httpServletRequest.getUserPrincipal().getName());
    Applikation applikation = appService.findApplikationByPerson(person);

    model.addAttribute("applikation" , applikation);

    return "/application/application";
  }


}
