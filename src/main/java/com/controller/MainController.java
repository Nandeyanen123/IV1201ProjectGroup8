package com.controller;

import com.model.Person;
import com.repository.PersonRepository;
import com.service.UpdatePersonValidator;
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
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * This is the MainController class.
 * It controls the addition of a person and the changing of a web page.
 */
@Controller
@RequestMapping(path="/")
public class MainController {

  @Autowired
  private PersonRepository personRepository;
  @Autowired
  private PersonValidator personValidator;

  /**
   * This method adds a new person and returns a String that is used to redirect.
   * @param name This is the first parameter of the addNewPerson method
   * @param surName This is the second parameter of the addNewPerson method
   * @param ssn This is the third parameter of the addNewPerson method
   * @param email This is the fourth parameter of the addNewPerson method
   * @param password This is fifth first parameter of the addNewPerson method
   * @param userName This is sixth first parameter of the addNewPerson method
   * @return String This returns the new person
   */
  @PostMapping(path="/register/add")
  public String addNewPerson (@NotNull @RequestParam String name, String surName, String ssn,
                              String email, String password, String userName){
    Person p = new Person();
    p.setName(name);
    p.setSurName(surName);
    p.setSsn(ssn);
    p.setEmail(email);
    p.setPassword(password);
    p.setRoleId(2);
    p.setUserName(userName);
    try {
      personRepository.save(p);
    } catch(Exception ex){
      return "redirect:/error";
    }
    //Change later to redirect to something good
    return "redirect:/";
  }

  /**
   * This method is used to get all the people from the repository and add their attributes to the model parameter.
   * @param model This is the only parameter of the getAllPeople method.
   * @return String this returns report1testresult
   */
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

}
