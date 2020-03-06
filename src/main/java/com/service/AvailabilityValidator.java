package com.service;

import com.model.Availability;
import com.repository.AvailabilityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

@Component
public class AvailabilityValidator implements Validator {
    Availability availability;
    Errors errors;
    ArrayList<Availability> availabilitiesFromDB = new ArrayList<>();
    @Autowired
    AvailabilityRepository availabilityRepository;

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
        availabilitiesFromDB = (ArrayList<Availability>) availabilityRepository.findAllByPersonId(availability.getPerson().getId());
    }

    //TODO Fix better validate for date
    private void validateDate() {
        if (availability.getFromDate().toString() == null)
            errors.rejectValue("getFromDate", "Please fill out valid from date");

        Iterator iterator = availabilitiesFromDB.iterator();

        while (iterator.hasNext()) {
            Availability current = (Availability) iterator.next();
            Date currFromDate = current.getFromDate();
            Date currToDate = current.getToDate();

            if (!availability.getFromDate().before(currToDate) && !availability.getFromDate().after(currFromDate))
                errors.rejectValue("fromDate", "Please fill out valid from date");

            else if(availability.getFromDate() == currFromDate || availability.getFromDate() == currToDate)
                errors.rejectValue("fromDate", "Please fill out valid from date");

            else if(availability.getFromDate() == currFromDate || availability.getFromDate() == currToDate){
                errors.rejectValue("fromDate", "Please fill out valid from date");}

            else if(availability.getToDate() == currFromDate || availability.getToDate() == currToDate)
                errors.rejectValue("fromDate", "Please fill out valid from date");

            else if(availability.getToDate() == currFromDate || availability.getToDate() == currToDate){
                errors.rejectValue("fromDate", "Please fill out valid from date");}

            else if (availability.getToDate().before(currToDate) && availability.getToDate().after(currFromDate))
                errors.rejectValue("toDate", "Please fill out valid to date");

        }
    }

    private void validatePersonId() {
        if(availability.getPerson() == null)
            errors.rejectValue("person", "Person cant be null");
    }
}
