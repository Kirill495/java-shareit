package ru.practicum.shareit.user;

import ru.practicum.shareit.user.dto.LightUserDto;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

public class UserMapper {

   public static UserDto toUserDto(User user) {
      if (user == null) {
         return null;
      }
      return UserDto.builder()
              .withId(user.getId())
              .withName(user.getName())
              .withEmail(user.getEmail())
              .build();
   }

   public static User toUser(UserDto userDto) {
      if (userDto == null) {
         return null;
      }
      return User.builder()
              .withId(userDto.getId())
              .withName(userDto.getName())
              .withEmail(userDto.getEmail())
              .build();
   }

   public static LightUserDto toLightUserDto(User user) {
      if (user == null) {
         return null;
      }
      return LightUserDto.builder()
              .withId(user.getId())
              .build();
   }
}
