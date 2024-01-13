package ru.practicum.shareit.user.dto;

import lombok.Getter;

import javax.validation.constraints.Email;

@Getter
public class UpdateUserRequest {

  private String name;

  @Email
  private String email;
}
