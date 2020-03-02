package com.service;

import com.model.Person;
import com.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * This class is used to validate a person(user)
 * The class implements Validator
 */
@Component
public class UpdatePersonValidator implements Validator {

    @Autowired
    PersonRepository personRepository;

    /**
     * This method is used to see if the Person class equals the class parameter.
     * @param clazz This is the only parameter of the method supports
     * @return boolean This returns true if Person class equals the parameter.
     */
    public boolean supports(Class clazz) {
        return Person.class.equals(clazz);
    }

    /**
     * This method accepts Object and Error parameters and do nothing.
     * @param target This is the first parameter of the method validate
     * @param errors This is the second parameter of the method validate
     */
    public void validate(Object target, Errors errors) {}

    /**
     * This method is used to validate a person name, surname, password and email
     * @param personFromDatabase This is the first parameter of the method validate
     * @param personWithNewData This is the second parameter of the method validate
     * @param errors This is the third parameter of the method validate
     */
    public void validate(Person personFromDatabase, Person personWithNewData, Errors errors){
        validateName(personFromDatabase, personWithNewData.getName(), errors);
        validateSurName(personFromDatabase, personWithNewData.getSurName(), errors);
        validatePasword(personFromDatabase, personWithNewData.getPassword(), errors);
        validateEmail(personFromDatabase, personWithNewData.getEmail(), errors);
    }

    /**
     * This method is used to validate a user email
     * @param personFromDatabase This is the first parameter of the method validateEmail
     * @param email This is the second parameter of the method validateEmail
     * @param errors This is the third parameter of the method validateEmail
     */
    private void validateEmail(Person personFromDatabase, String email, Errors errors) {
        String regex = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$";
        if(email.matches(regex))
            personFromDatabase.setEmail(email);
        else
            errors.rejectValue("email", "email.is.invalid");
    }

    /**
     * This method is used to validate a user email
     * @param personFromDatabase This is the first parameter of the method validateName
     * @param name This is the second parameter of the method validateName
     * @param errors This is the third parameter of the method validateName
     */
    private void validateName(Person personFromDatabase, String name, Errors errors) {
        if(nameTest(name))
            personFromDatabase.setName(name);
        else
            errors.rejectValue("name", "name.is.invalid");
    }

    /**
     * This method is used to validate a user surname
     * @param personFromDatabase This is the first parameter of the method validateSurName
     * @param userName This is the first parameter of the method validateSurName
     * @param errors This is the first parameter of the method validateSurName
     */
    private void validateSurName(Person personFromDatabase, String userName, Errors errors) {
        if(nameTest(userName))
            personFromDatabase.setSurName(userName);
        else
            errors.rejectValue("surName", "surname.is.invalid");
    }

    /**
     * This method checks if a person name length is more than 0 and less than 45.
     * @param name This is the only parameter in the nameTest method
     * @return boolean This returns a boolean
     */
    private boolean nameTest(String name){
        if(name == null)
            return false;
        else if(name.length() > 0 && name.length() < 45 && name.matches("^[a-zA-ZåäöÅÄÖ]*$"))
            return true;
        else
            return false;
    }

    /**
     * This method is used to validate a user password
     * @param personFromDatabase This is the first parameter of the method validatePasword
     * @param newPassword This is the first parameter of the method validatePasword
     * @param errors This is the first parameter of the method validatePasword
     */
    private void validatePasword(Person personFromDatabase, String newPassword, Errors errors) {
        if(newPassword == null)
            return;

        if(newPassword.length() > 4 && newPassword.length() < 45 )
            personFromDatabase.setPassword(newPassword);
        else if(newPassword.length() < 4 || newPassword.length() > 45)
            errors.rejectValue("password" , "password.is.bad.size");
    }
}