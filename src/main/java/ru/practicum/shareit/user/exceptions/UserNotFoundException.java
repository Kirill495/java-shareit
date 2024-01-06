package ru.practicum.shareit.user.exceptions;

public class UserNotFoundException extends RuntimeException {
   public UserNotFoundException(String message) {
      super(message);
   }

   public UserNotFoundException(int userId) {
      super(String.format("Пользователь с id \"%d\" не существует", userId));
   }
}
