package ru.practicum.shareit.item.exceptions;

public class UserItemNotFoundException extends ItemNotFoundException {
  public UserItemNotFoundException(String message) {
    super(message);
  }

  public UserItemNotFoundException(int itemId, int userId) {
    this(String.format("Предмет %d пользователя %d не найден", itemId, userId));
  }

}
