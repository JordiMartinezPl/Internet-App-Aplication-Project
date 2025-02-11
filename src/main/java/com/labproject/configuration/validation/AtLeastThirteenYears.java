package com.labproject.configuration.validation;

import com.nimbusds.jose.Payload;
import jakarta.validation.Constraint;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = AtLeastThirteenYearsValidator.class)
@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface AtLeastThirteenYears {
    String message() default "You must be at least thirteen years.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
