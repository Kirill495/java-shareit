package ru.practicum.shareit.user.mapper;

import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.factory.Mappers;
import ru.practicum.shareit.user.dto.LightOutputUser;
import ru.practicum.shareit.user.dto.NewUserRequest;
import ru.practicum.shareit.user.dto.OutputUser;
import ru.practicum.shareit.user.dto.UpdateUserRequest;
import ru.practicum.shareit.user.entity.UserEntity;
import ru.practicum.shareit.user.model.User;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, builder = @Builder(disableBuilder = true))
public interface UserMapper {

  UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

  @Mapping(target = "id", ignore = true)
  User toModel(NewUserRequest userRequest);

  @Mapping(target = "id", ignore = true)
  User toModel(UpdateUserRequest userRequest);

  User toModel(UserEntity userEntity);

  List<User> toModel(List<UserEntity> userEntities);

  UserEntity toEntity(User user);

  OutputUser toOutputUser(User user);

  List<OutputUser> toOutputUser(List<User> users);

  LightOutputUser toLightOutputUser(User user);

}
