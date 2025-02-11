package com.labproject.configuration.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;
import java.time.Period;

public class AtLeastThirteenYearsValidator implements ConstraintValidator<AtLeastThirteenYears, LocalDate> {

    @Override
    public boolean isValid(LocalDate dateField, ConstraintValidatorContext context) {
        if (dateField == null) {
            return true;
        }
        return Period.between(dateField, LocalDate.now()).getYears() >= 13;
    }
}
