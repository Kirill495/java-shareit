package ru.practicum.shareit.user.dto;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import ru.practicum.shareit.user.validators.AtLeastOneNonNullField;

import javax.validation.constraints.Email;

@Getter
@EqualsAndHashCode(of = {"name", "email"})
@AllArgsConstructor
@AtLeastOneNonNullField
public class UpdateUserRequest {

  private String name;

  @Email
  private String email;
}
