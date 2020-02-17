package com.controller;

import com.model.Person;
import com.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


@Controller
@RequestMapping(path="/")
public class MainController {

  @Autowired
  private PersonRepository personRepository;

  @PostMapping(path="/report1test/add")
  public String addNewPerson (@Nullable @RequestParam String name, String surName, String ssn,
                              String email, String password, Integer roleId, String userName){
    Person p = new Person();
    p.setName(name);
    p.setSurName(surName);
    p.setSsn(ssn);
    p.setEmail(email);
    p.setPassword(password);
    p.setRoleId(roleId);
    p.setUserName(userName);
    try {
        personRepository.save(p);
    } catch(Exception ex){
        return "redirect:/error";
    }
    return "redirect:/report1testresult";
  }

  @GetMapping(path="/report1testresult")
  public String getAllPeople(Model model){
      Iterable<Person> people = personRepository.findAll();
      model.addAttribute("people",people);
    return "report1testresult";
  }


    @GetMapping("/report1test")
    public String personForm(Model model) {
        model.addAttribute("person", new Person());
        return "report1test";
    }

    @PostMapping("/report1test")
    public String personSubmit(@ModelAttribute Person person) {
        return "report1testresult";
    }

  @RequestMapping("/")
  String index() {
    return "index";
  }

  @RequestMapping("/report")
  String repTest() {
    return "report1test";
  }

  @GetMapping("/login")
  public String userLogin(){
    return "login";
  }
}
