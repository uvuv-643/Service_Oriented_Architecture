package ru.uvuv643.web;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.ArrayList;
import java.util.List;

public class EnumValueValidator implements ConstraintValidator<EnumValue, String> {
    private List<String> acceptedValues = new ArrayList<>();

    @Override
    public void initialize(EnumValue constraintAnnotation) {
        for (Enum<?> enumValue : constraintAnnotation.enumClass().getEnumConstants()) {
            acceptedValues.add(enumValue.name());
        }
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return value == null || acceptedValues.contains(value);
    }
}