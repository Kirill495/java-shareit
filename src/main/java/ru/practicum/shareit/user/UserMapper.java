package ru.practicum.shareit.user;

import ru.practicum.shareit.user.dto.UserDto;

public class UserMapper {
   public static UserDto toUserDto(User user) {
      return UserDto.builder()
              .withId(user.getId())
              .withName(user.getName())
              .withEmail(user.getEmail())
              .build();
   }
}
