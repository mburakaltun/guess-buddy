package com.mburakaltun.guessbuddy.feedback.model.converter;

import com.mburakaltun.guessbuddy.feedback.model.enums.FeedbackCategory;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class FeedbackCategoryConverter implements AttributeConverter<FeedbackCategory, Integer> {
    @Override
    public Integer convertToDatabaseColumn(FeedbackCategory category) {
        if (category == null) {
            return null;
        }
        return category.getCode();
    }

    @Override
    public FeedbackCategory convertToEntityAttribute(Integer dbData) {
        if (dbData == null) {
            return null;
        }

        for (FeedbackCategory category : FeedbackCategory.values()) {
            if (category.getCode() == dbData) {
                return category;
            }
        }

        throw new IllegalArgumentException("Unknown database value: " + dbData);
    }
}