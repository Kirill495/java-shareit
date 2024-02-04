package ru.practicum.shareit.user.dto;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@AllArgsConstructor
@Getter
@EqualsAndHashCode(of = {"name", "email"})
public class NewUserRequest {

  @NotNull
  @NotBlank
  private String name;

  @NotNull
  @NotEmpty
  @Email
  private String email;

}
