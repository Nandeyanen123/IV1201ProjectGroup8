package com.controller;

import com.model.Person;
import com.service.PersonRepository;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Map;

@Controller
//@SpringBootApplication
@RequestMapping(path="/")
public class MainController {

  @Autowired
  private Person p;
  @Autowired
  private PersonRepository personRepository;

  @PostMapping(path="/report1test/add")
  public String addNewPerson (@Nullable @RequestParam String name, String surName, String ssn,
                              String email, String password, Integer roleId, String userName){
    //Person p = new Person();
    p.setName(name);
    p.setSurName(surName);
    p.setSsn(ssn);
    p.setEmail(email);
    p.setPassword(password);
    p.setRoleId(roleId);
    p.setUserName(userName);
    personRepository.save(p);
    return "redirect:/report1testresult";
  }

  @GetMapping(path="/report1testresult")
  //public @ResponseBody String getAllPeople(Model model){
      public String getAllPeople(Model model){
      Iterable<Person> people = personRepository.findAll();
      model.addAttribute("people",people);
    return "report1testresult";
  }

  /*public static void main(String[] args) throws Exception {
    SpringApplication.run(MainController.class, args);
  }*/

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


  /*@RequestMapping("/db")
  String db(Map<String, Object> model) {
    try (Connection connection = dataSource.getConnection()) {
      Statement stmt = connection.createStatement();
      stmt.executeUpdate("CREATE TABLE IF NOT EXISTS ticks (tick timestamp)");
      stmt.executeUpdate("INSERT INTO ticks VALUES (now())");
      ResultSet rs = stmt.executeQuery("SELECT tick FROM ticks");

      ArrayList<String> output = new ArrayList<String>();
      while (rs.next()) {
        output.add("Read from DB: " + rs.getTimestamp("tick"));
      }

      model.put("records", output);
      return "db";
    } catch (Exception e) {
      model.put("message", e.getMessage());
      return "error";
    }
  }

  @Bean
  public DataSource dataSource() throws SQLException {
    if (dbUrl == null || dbUrl.isEmpty()) {
      return new HikariDataSource();
    } else {
      HikariConfig config = new HikariConfig();
      config.setJdbcUrl(dbUrl);
      return new HikariDataSource(config);
    }
  }*/

}
