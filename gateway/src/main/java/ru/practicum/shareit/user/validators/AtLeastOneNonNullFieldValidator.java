package ru.practicum.shareit.user.validators;

import ru.practicum.shareit.user.dto.UpdateUserRequest;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class AtLeastOneNonNullFieldValidator implements ConstraintValidator<AtLeastOneNonNullField, UpdateUserRequest> {

  @Override
  public void initialize(AtLeastOneNonNullField constraintAnnotation) {
    ConstraintValidator.super.initialize(constraintAnnotation);
  }

  @Override
  public boolean isValid(UpdateUserRequest value, ConstraintValidatorContext constraintValidatorContext) {
    return (value.getEmail() != null && !value.getEmail().isBlank())
            || (value.getName() != null && !value.getName().isBlank());
  }
}
