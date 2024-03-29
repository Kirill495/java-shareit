package ru.practicum.shareit.item.dto.comment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(setterPrefix = "with")
public class OutputCommentDto {
  private Integer id;
  private String text;
  private String authorName;
  private String created;
}
