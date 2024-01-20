package ru.practicum.shareit.request.dto;

import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
public class InputItemRequestDto {

  @NotBlank
  @NotNull
  private String description;
}
