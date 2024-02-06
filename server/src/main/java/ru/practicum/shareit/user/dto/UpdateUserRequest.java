package ru.practicum.shareit.user.dto;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode(of = {"name", "email"})
@AllArgsConstructor
public class UpdateUserRequest {
  private String name;
  private String email;
}
