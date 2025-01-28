package ru.uvuv643.web;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import ru.uvuv643.web.EnumValueValidator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = EnumValueValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface EnumValue {
    Class<? extends Enum<?>> enumClass();
    String message() default "must be a valid enum (check DTO)";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}