package ru.practicum.shareit.item.exceptions;

public class UserItemNotFoundException extends RuntimeException {
  public UserItemNotFoundException(String message) {
    super(message);
  }
}
