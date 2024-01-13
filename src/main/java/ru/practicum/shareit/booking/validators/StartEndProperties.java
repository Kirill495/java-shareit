package ru.practicum.shareit.booking.validators;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;


@Target({ElementType.TYPE})
@Retention(RUNTIME)
@Constraint(validatedBy = StartEndPropertiesValidator.class)
@Documented
public @interface StartEndProperties {

  String message() default "Дата окончания меньше даты старта";
  Class<?>[] groups() default {};
  Class<? extends Payload>[] payload() default {};

}
