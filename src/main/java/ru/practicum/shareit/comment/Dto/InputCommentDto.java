package ru.practicum.shareit.comment.Dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class InputCommentDto {
  @NotNull
  @NotBlank
  private String text;
}
