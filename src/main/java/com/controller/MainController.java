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


@Controller
@RequestMapping(path="/")
public class MainController {

  @Autowired
  private PersonRepository personRepository;
  @Autowired
  private PersonValidator personValidator;

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


  @GetMapping(path="/report1testresult")
  public String getAllPeople(Model model){
      Iterable<Person> people = personRepository.findAll();
      model.addAttribute("people",people);
    return "report1testresult";
  }


  @GetMapping("/register")
  public String personForm(Model model) {
    model.addAttribute("person", new Person());
    return "register";
  }


  @PostMapping("/register")
  public String personSubmit(@ModelAttribute Person person) {
    return "register";
  }


  @RequestMapping("/index")
  String index() {
    return "index";
  }

  @RequestMapping("/report")
  String repTest() {
    return "report1test";
  }

  @RequestMapping("/")
  public String homePage(){
    return "index";
  }

  @RequestMapping("/login")
  public String userLogin(){
    return "login";
  }

  @RequestMapping("/profile")
  public String userProfile(HttpServletRequest httpServletRequest, Model model){
    Person person = personRepository.findByUserName(httpServletRequest.getUserPrincipal().getName());
    model.addAttribute("person" , person);
    return "profile";
  }

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

  @RequestMapping("/lockedpage")
  public String lockedPage(){
    return "lockedpage";
  }

}
