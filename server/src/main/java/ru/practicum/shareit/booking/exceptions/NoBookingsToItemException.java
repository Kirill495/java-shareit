package ru.practicum.shareit.booking.exceptions;

public class NoBookingsToItemException extends RuntimeException {
  public NoBookingsToItemException(String message) {
    super(message);
  }
}
