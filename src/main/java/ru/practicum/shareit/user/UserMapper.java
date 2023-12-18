package ru.practicum.shareit.user;

import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

public class UserMapper {

   public static UserDto toUserDto(User user) {
      return UserDto.builder()
              .withId(user.getId())
              .withName(user.getName())
              .withEmail(user.getEmail())
              .build();
   }

   public static User toUser(UserDto userDto) {
      return User.builder()
              .withId(userDto.getId())
              .withName(userDto.getName())
              .withEmail(userDto.getEmail())
              .build();
   }
}
