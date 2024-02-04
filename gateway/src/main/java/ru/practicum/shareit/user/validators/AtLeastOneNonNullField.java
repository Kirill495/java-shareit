package ru.practicum.shareit.user.validators;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({ElementType.TYPE})
@Retention(RUNTIME)
@Constraint(validatedBy = AtLeastOneNonNullFieldValidator.class)
@Documented
public @interface AtLeastOneNonNullField {
  String message() default "Хотя бы одно поле должно быть не пустым";
  Class<?>[] groups() default {};
  Class<? extends Payload>[] payload() default {};
}
