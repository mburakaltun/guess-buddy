package com.mburakaltun.guessbuddy.common.annotation;

import com.mburakaltun.guessbuddy.common.validator.CleanContentValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = CleanContentValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface CleanContent {

    String message();

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}