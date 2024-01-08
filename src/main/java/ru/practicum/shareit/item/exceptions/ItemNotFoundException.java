package ru.practicum.shareit.item.exceptions;

public class ItemNotFoundException extends RuntimeException {
  public ItemNotFoundException(String message) {
    super(message);
  }

  public ItemNotFoundException(int itemId) {
    this(String.format("Предмет %d не найден", itemId));
  }
}
