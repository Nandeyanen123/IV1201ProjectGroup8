package com.service;

import com.Error.DatabaseExceptions;
import com.model.Availability;
import com.repository.AvailabilityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Component
public class AvailabilityValidator implements Validator {
    Availability availability;
    Errors errors;
    ArrayList<Availability> availabilitiesFromDB = new ArrayList<>();

    @Autowired
    RecruitmentAppService appService;

    @Override
    public boolean supports(Class<?> clazz) {
        return false;
    }

    @Override
    public void validate(Object target, Errors errors) {
        this.errors = errors;
        this.availability = (Availability) target;

        validatePersonId();

        try {
            getAllAvailability();
        } catch (DatabaseExceptions databaseExceptions) {
            databaseExceptions.printStackTrace();
        }

        validateDate();
    }

    private void getAllAvailability() throws DatabaseExceptions {
        availabilitiesFromDB = (ArrayList<Availability>) appService.findAllAvailabilityByPersonId(availability.getPerson().getId());
    }

    //TODO Fix better validate for date
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

    private void validatePersonId() {
        if(availability.getPerson() == null)
            errors.rejectValue("person", "Person can't be null");
    }
}
