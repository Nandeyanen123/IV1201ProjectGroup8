package com.service;

import com.model.Person;
import com.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;


@Component
public class UpdatePersonValidator implements Validator {

    @Autowired
    PersonRepository personRepository;
    public boolean supports(Class clazz) {
        return Person.class.equals(clazz);
    }
    public void validate(Object target, Errors errors) {}

    public void validate(Person personFromDatabase, Person personWithNewData, Errors errors){
        validateName(personFromDatabase, personWithNewData.getName(), errors);
        validateSurName(personFromDatabase, personWithNewData.getSurName(), errors);
        validatePasword(personFromDatabase, personWithNewData.getPassword(), errors);
        validateEmail(personFromDatabase, personWithNewData.getEmail(), errors);
    }

    private void validateEmail(Person personFromDatabase, String email, Errors errors) {
        String regex = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$";
        if(email.matches(regex))
            personFromDatabase.setEmail(email);
        else
            errors.rejectValue("email", "email.is.invalid");
    }

    private void validateName(Person personFromDatabase, String name, Errors errors) {
        if(nameTest(name))
            personFromDatabase.setName(name);
        else
            errors.rejectValue("name", "name.is.invalid");
    }

    private void validateSurName(Person personFromDatabase, String userName, Errors errors) {
        if(nameTest(userName))
            personFromDatabase.setSurName(userName);
        else
            errors.rejectValue("surName", "surname.is.invalid");
    }

    private boolean nameTest(String name){
        if(name == null)
            return false;
        else if(name.length() > 0 && name.length() < 45 && name.matches("^[a-zA-ZåäöÅÄÖ]*$"))
            return true;
        else
            return false;
    }

    private void validatePasword(Person personFromDatabase, String newPassword, Errors errors) {
        if(newPassword == null)
            return;

        if(newPassword.length() > 4 && newPassword.length() < 45 )
            personFromDatabase.setPassword(newPassword);
        else if(newPassword.length() < 4 || newPassword.length() > 45)
            errors.rejectValue("password" , "password.is.bad.size");
    }
}