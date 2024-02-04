package ru.practicum.shareit.item.mapper;

import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;
import ru.practicum.shareit.item.dto.comment.InputCommentDto;
import ru.practicum.shareit.item.dto.comment.OutputCommentDto;
import ru.practicum.shareit.item.entity.CommentEntity;
import ru.practicum.shareit.item.model.Comment;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        builder = @Builder(disableBuilder = true))
public interface CommentMapper {

  CommentMapper MAPPER = Mappers.getMapper(CommentMapper.class);

  Comment toModel(CommentEntity entity);

  Comment toModel(InputCommentDto dto);

  CommentEntity toEntity(Comment model);

  @Mapping(target = "authorName", source = "author.name")
  OutputCommentDto toDto(Comment model);
}
