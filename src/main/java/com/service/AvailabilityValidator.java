package com.service;

import com.Error.DatabaseExceptions;
import com.controller.MainController;
import com.model.Availability;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;

/**
 * This class is called AvailabilityValidator and it implements validator
 */
@Component
public class AvailabilityValidator implements Validator {
    Availability availability;
    Errors errors;
    ArrayList<Availability> availabilitiesFromDB = new ArrayList<>();

    @Autowired
    RecruitmentAppService appService;

    /**
     * This class checks if supports is true or false
     * @param clazz This is the only parameter of the method supports
     * @return boolean returns false
     */
    @Override
    public boolean supports(Class<?> clazz) {
        return false;
    }


    @Override
    public void validate(Object target, Errors errors){}

    /**
     * This method gets all the availability
     * @throws DatabaseExceptions
     */
    private void getAllAvailability() throws DatabaseExceptions {
        availabilitiesFromDB = (ArrayList<Availability>) appService.findAllAvailabilityByPersonId(availability.getPerson().getId());
    }

    //TODO Fix better validate for date

    /**
     * This method validates the date
     */
    private void validateDate() {
        LocalDate current = LocalDate.now();
        DateFormat originalFormat = new SimpleDateFormat("EE MMM dd HH:mm:ss z yyyy");
        DateFormat targetFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date toDate = originalFormat.parse(availability.getToDate().toString());
            String formatDate = targetFormat.format(toDate);
            LocalDate compDate = LocalDate.parse(formatDate);
            if (compDate.isBefore(current)) {
                errors.rejectValue("toDate", "You have to be available in the future, not in the past.");
            }
        }catch(ParseException ex){
            ex.printStackTrace();
        }
        if (availability.getFromDate().toString() == null)
            errors.rejectValue("fromDate", "Please fill out valid from date");

        if (availability.getFromDate().toString() == null)
            errors.rejectValue("fromDate", "Please fill out valid from date");

    }

    /**
     * This method validates the person id
     */
    private void validatePersonId() {
        if(availability.getPerson() == null)
            errors.rejectValue("person", "Person can't be null");
    }

    /**
     * Validates data on new Availability object
     * @param newAvailability data to be validated
     * @param errors contains all errors
     * @throws DatabaseExceptions throws exception if databas fails
     */
    public void validateDates(Availability newAvailability, Errors errors) throws DatabaseExceptions {
        this.errors = errors;
        this.availability = newAvailability;

        validatePersonId();
        getAllAvailability();
        validateDate();
    }
}
