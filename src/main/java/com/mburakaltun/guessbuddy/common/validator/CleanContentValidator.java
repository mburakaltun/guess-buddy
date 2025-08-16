package com.mburakaltun.guessbuddy.common.validator;

import com.mburakaltun.guessbuddy.common.annotation.CleanContent;
import com.mburakaltun.guessbuddy.common.service.CleanContentService;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CleanContentValidator implements ConstraintValidator<CleanContent, String> {

    private final CleanContentService cleanContentService;

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.trim().isEmpty()) {
            return true;
        }

        try {
            cleanContentService.validateContent(value);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
