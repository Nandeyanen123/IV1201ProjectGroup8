package com.controller;

import com.DAO.competenceProfileCompetenceYearDAO;
import com.model.Competence;
import com.model.Competence_Profile;
import com.model.Person;
import com.repository.CompetenceProfileRepository;
import com.repository.CompetenceRepository;
import com.repository.PersonRepository;
import com.service.UpdatePersonValidator;
import com.service.PersonValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import com.service.PersonValidator;
import org.springframework.web.bind.support.SessionStatus;
import javax.servlet.http.HttpServletRequest;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.*;

/**
 * This is the MainController class.
 * It controls the addition of a person and the changing of a web page.
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

  /**
   * This method adds a new person and returns a String that is used to redirect.
   * @param person person that should be added
   * @param result contains error handling
   * @param status
   * @return
   */
  @RequestMapping(path = "/register/add", method = RequestMethod.POST)
    public String addNewPerson(@Valid Person person, BindingResult result, SessionStatus status){

    // To Create own validate
    personValidator.validate(person,result);
    if(result.hasErrors()){
      return "register";
    }else{
        person.setRoleId(2);
        personRepository.save(person);
        status.setComplete();
        return "redirect:/success";
    }
  }

  /**
   * This method is used to get all the people from the repository and add their attributes to the model parameter.
   * @param model This is the only parameter of the getAllPeople method.
   * @return String this returns report1testresult
   */
  @RequestMapping(value = "/success", method = RequestMethod.GET)
  public String success(Model model)
  {
    return "addSuccess";
  }

  @GetMapping(path="/report1testresult")
  public String getAllPeople(Model model){
      Iterable<Person> people = personRepository.findAll();
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
  @PostMapping("/register")
  public String personSubmit(@ModelAttribute Person person) {
    return "register";
  }

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
    Person person = personRepository.findByUserName(httpServletRequest.getUserPrincipal().getName());
    model.addAttribute("person" , person);
    return "profile";
  }

  /**
   * This is used to return user logout.
   * @return String returns logout
   */
  @RequestMapping(value = "/profile/profile_update", method = RequestMethod.PUT)
  public String profileUpdate(Person personFromFrom, HttpServletRequest httpServletRequest, Model model,  BindingResult result, SessionStatus status){
    String username = httpServletRequest.getUserPrincipal().getName();
    Person personFromDatabase = personRepository.findByUserName(username);
    UpdatePersonValidator updatePersonValidator = new UpdatePersonValidator();

    updatePersonValidator.validate(personFromDatabase, personFromFrom ,result);

    if(result.hasErrors())
      return "profile/profile_update";

    else
      personRepository.save(personFromDatabase);
    return "profile";
  }


  @RequestMapping(value = "/profile/profile_update", method = RequestMethod.GET)
  public String profileUpdate2(Model model, HttpServletRequest httpServletRequest){
    Person person = personRepository.findByUserName(httpServletRequest.getUserPrincipal().getName());
    model.addAttribute("person" , person);
    return "profile/profile_update";
  }

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

  @RequestMapping(value = "/profile/profile_competence", method = RequestMethod.GET)
  public String profileCompetence(Model model, HttpServletRequest httpServletRequest) {
    String username = httpServletRequest.getUserPrincipal().getName();
    Person person = personRepository.findByUserName(username);

    Iterable<Competence_Profile> competence_Profile = competenceProfileRepository.findAllByPersonId(person.getId());
    Iterable<Competence> competence = competenceRepository.findAll();

    competenceProfileCompetenceYearDAO test = new competenceProfileCompetenceYearDAO();
    Map<String, Integer> map = test.getCompetenceNameAndYear(person.getId(),competence_Profile, competence);

    model.addAttribute("competence" , competence);
    model.addAttribute("map" , map);
    return "/profile/profile_competence";
  }

  @RequestMapping(value = "/profile/profile_competence/delete/{componentName}")
  public String profile_competence_delete(HttpServletRequest httpServletRequest, @PathVariable String componentName){
    String username = httpServletRequest.getUserPrincipal().getName();
    Person p = personRepository.findByUserName(username);
    Competence competence = competenceRepository.findByCompetenceName(componentName);
    Competence_Profile profile = competenceProfileRepository.findByPersonAndCompetence(p, competence);
    competenceProfileRepository.delete(profile);
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


  @RequestMapping(value = "/profile/profile_competence/add", method = RequestMethod.POST)
  public String profile_competence_add(HttpServletRequest httpServletRequest, Competence competence){
    String username = httpServletRequest.getUserPrincipal().getName();
    Person p = personRepository.findByUserName(username);

    String year = httpServletRequest.getParameter("year");
    int yearInt = Integer.parseInt(year);
    Competence_Profile checkForDuplicateCompetence = competenceProfileRepository.findByPersonAndCompetence(p,competence);

    if(checkForDuplicateCompetence == null) {
      Competence_Profile newProfile = new Competence_Profile(p, competence, yearInt);
      competenceProfileRepository.save(newProfile);
    }

    return "redirect:/profile/profile_competence";
  }


}
