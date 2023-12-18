package ru.practicum.shareit.user.exceptions;

public class UserDuplicateEmailException extends RuntimeException {
   public UserDuplicateEmailException(String message) {
      super(message);
   }
}
