package ru.practicum.shareit.booking.exceptions;

public class BookingSetStatusByOwnerException extends RuntimeException {
  public BookingSetStatusByOwnerException(String message) {
    super(message);
  }
}
