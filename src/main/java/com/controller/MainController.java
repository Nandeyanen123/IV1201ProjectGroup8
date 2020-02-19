package com.controller;

import com.model.Person;
import com.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;


@Controller
@RequestMapping(path="/")
public class MainController {

  @Autowired
  private PersonRepository personRepository;

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
  public String userLogin()
  {

    return "login";
  }

  @RequestMapping("/logout-success")
  public String logoutPage(){
    return "logout";
  }

}
