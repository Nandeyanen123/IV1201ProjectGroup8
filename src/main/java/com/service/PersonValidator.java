package com.service;

import com.model.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import javax.transaction.Transactional;

/**
 * This class is a person validator.
 * It implements Validator.
 */
@Component
public class PersonValidator implements Validator {

    @Autowired
    RecruitmentAppService appService;

    /**
     * This method checks if support is true or not.
     *
     * @param clazz This is the only parameter of the method supports
     * @return boolean This returns true a person class equals the parameter.
     */
    public boolean supports(Class clazz) {
        return Person.class.equals(clazz);
    }

    /**
     * This method validate if the users ssn and username is taken or not. If it is then
     * an error will show up.
     *
     * @param target This is the first parameter of the method validate
     * @param errors This is the second parameter of the method validate
     */
    @Override
    @Transactional
    public void validate(Object target, Errors errors) {
        Person p = (Person) target;
        if (appService.personExistsBySSN(p.getSsn()) == true)
            errors.rejectValue("ssn", "ssn.is.already.taken");

        if (appService.personExistsByUsername(p.getUserName()) == true)
            errors.rejectValue("userName", "username.is.already.taken");

            /* Support for uniq email
            if(personRepository.findByEmail(p.getEmail())){
                errors.rejectValue("email" ,"email.is.already.taken");
            }
             */
    }
}
