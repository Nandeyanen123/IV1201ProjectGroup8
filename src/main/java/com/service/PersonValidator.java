package com.service;

import com.model.Person;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import javax.persistence.criteria.CriteriaBuilder;


@Component
public class PersonValidator implements Validator {

    public boolean supports(Class clazz) {
        return Person.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        ValidationUtils.rejectIfEmptyOrWhitespace(errors,"name","error.name", "Name field can not be empty");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors,"sureName","error.sureName", "Surename field can not be empty");
        Person p = (Person) target;
        String username = p.getUserName();
        int userLength = username.length();
        String password = p.getPassword();
        int  passLength = password.length();

        if(userLength < 4 || userLength > 8) {
            errors.rejectValue("username", "username must be between 4 to 8");
        }
        else if(passLength < 5){

            errors.rejectValue("password", "password can not be less then 5");

        }
    }
}
