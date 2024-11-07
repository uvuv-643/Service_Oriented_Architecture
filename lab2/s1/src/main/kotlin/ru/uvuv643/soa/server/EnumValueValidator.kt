package ru.uvuv643.soa.server

import EnumValue
import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext

class EnumValueValidator : ConstraintValidator<EnumValue, String> {
    private val acceptedValues: MutableList<String> = mutableListOf()

    override fun initialize(constraintAnnotation: EnumValue) {
        super.initialize(constraintAnnotation)
        acceptedValues.addAll(constraintAnnotation.enumClass.java.enumConstants.map { it.name })
    }

    override fun isValid(value: String?, context: ConstraintValidatorContext?): Boolean {
        return if (value == null) {
            true
        } else acceptedValues.contains(value.toString())
    }
}