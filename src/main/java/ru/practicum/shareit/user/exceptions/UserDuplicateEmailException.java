package ru.practicum.shareit.user.exceptions;

import ru.practicum.shareit.user.model.User;

public class UserDuplicateEmailException extends RuntimeException {
   public UserDuplicateEmailException(String message) {
      super(message);
   }

   public UserDuplicateEmailException(User user) {
      this(String.format("Пользователь с email \"%s\" уже существует", user.getEmail()));
   }
}
