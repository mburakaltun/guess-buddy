package com.mburakaltun.guessbuddy.common.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class StatusConverter implements AttributeConverter<Status, Integer> {

    @Override
    public Integer convertToDatabaseColumn(Status status) {
        if (status == null) {
            return null;
        }
        return status.getValue();
    }

    @Override
    public Status convertToEntityAttribute(Integer value) {
        if (value == null) {
            return null;
        }

        for (Status status : Status.values()) {
            if (status.getValue() == value) {
                return status;
            }
        }

        throw new IllegalArgumentException("Unknown database value: " + value);
    }
}