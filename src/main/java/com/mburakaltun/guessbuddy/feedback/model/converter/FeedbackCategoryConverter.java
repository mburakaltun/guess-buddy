package com.mburakaltun.guessbuddy.feedback.model.converter;

import com.mburakaltun.guessbuddy.feedback.model.enums.FeedbackCategory;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class FeedbackCategoryConverter implements AttributeConverter<FeedbackCategory, String> {
    @Override
    public String convertToDatabaseColumn(FeedbackCategory category) {
        if (category == null) {
            return null;
        }
        return category.name();
    }

    @Override
    public FeedbackCategory convertToEntityAttribute(String dbData) {
        if (dbData == null || dbData.isEmpty()) {
            return null;
        }
        try {
            return FeedbackCategory.valueOf(dbData);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Unknown database value: " + dbData, e);
        }
    }
}
