package com.service;

import com.model.Person;
import com.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import javax.persistence.criteria.CriteriaBuilder;


@Component
public class PersonValidator implements Validator {

    @Autowired
    PersonRepository personRepository;
    public boolean supports(Class clazz) {
        return Person.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Person p = (Person) target;

        if(personRepository.findBySsn(p.getSsn()) != null)
            errors.rejectValue("ssn" ,"ssn.is.already.taken");

        if(personRepository.findByUserName(p.getUserName()) != null)
            errors.rejectValue("userName" ,"username.is.already.taken");

            /* Support for uniq email
            if(personRepository.findByEmail(p.getEmail())){
                errors.rejectValue("email" ,"email.is.already.taken");
            }
             */
    }
}