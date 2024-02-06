package ru.practicum.shareit.item.dto.comment;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class InputCommentDto {
  @NotNull
  @NotBlank
  private String text;
}
