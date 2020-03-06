package com.service;

import com.model.Availability;
import com.repository.AvailabilityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

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
        getAllAvailability();
        validateDate();
    }

    private void getAllAvailability() {
        availabilitiesFromDB = (ArrayList<Availability>) appService.findAllAvailabilityByPersonId(availability.getPerson().getId());
    }

    //TODO Fix better validate for date
    private void validateDate() {
        if (availability.getFromDate().toString() == null)
            errors.rejectValue("getFromDate", "Please fill out valid from date");

        if (availability.getFromDate().toString() == null)
            errors.rejectValue("getFromDate", "Please fill out valid from date");

    }

    private void validatePersonId() {
        if(availability.getPerson() == null)
            errors.rejectValue("person", "Person cant be null");
    }
}
