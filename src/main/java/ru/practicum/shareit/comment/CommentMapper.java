package ru.practicum.shareit.comment;

import ru.practicum.shareit.comment.Dto.InputCommentDto;
import ru.practicum.shareit.comment.Dto.OutputCommentDto;

import java.time.format.DateTimeFormatter;

public class CommentMapper {
  public static Comment toComment(InputCommentDto inputCommentDto) {
    return Comment.builder()
            .withText(inputCommentDto.getText())
            .build();
  }

  public static OutputCommentDto toCommentDto(Comment comment) {
    return OutputCommentDto.builder()
            .withId(comment.getId())
            .withAuthorName(comment.getAuthor().getName())
            .withText(comment.getText())
            .withCreated(comment.getCreated().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))
            .build();
  }
}
