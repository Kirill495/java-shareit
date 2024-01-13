package ru.practicum.shareit.item.validators;

import ru.practicum.shareit.item.dto.item.UpdateItemRequest;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class AtLeastOneNonNullFieldValidator implements ConstraintValidator<AtLeastOneNonNullField, UpdateItemRequest> {

  @Override
  public void initialize(AtLeastOneNonNullField constraintAnnotation) {
    ConstraintValidator.super.initialize(constraintAnnotation);
  }

  @Override
  public boolean isValid(UpdateItemRequest value, ConstraintValidatorContext constraintValidatorContext) {
    return value.getAvailable() != null
            || value.getDescription() != null
            || value.getName() != null;
  }
}
