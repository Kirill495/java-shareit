package ru.practicum.shareit.item;

public class NoBookingsToItemException extends RuntimeException {
  public NoBookingsToItemException(String message) {
    super(message);
  }
}
