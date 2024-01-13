package ru.practicum.shareit.booking.validators;

import ru.practicum.shareit.booking.dto.InputBookingRequest;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class StartEndPropertiesValidator implements ConstraintValidator<StartEndProperties, InputBookingRequest> {

  @Override
  public void initialize(StartEndProperties constraintAnnotation) {
    ConstraintValidator.super.initialize(constraintAnnotation);
  }

  @Override
  public boolean isValid(InputBookingRequest value, ConstraintValidatorContext constraintValidatorContext) {
    return value.getStart() != null
            && value.getEnd() != null
            && value.getStart().isBefore(value.getEnd());
  }
}
