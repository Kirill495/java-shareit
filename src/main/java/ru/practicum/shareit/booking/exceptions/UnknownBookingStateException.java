package ru.practicum.shareit.booking.exceptions;

public class UnknownBookingStateException extends RuntimeException {
  public UnknownBookingStateException(String status) {
    super("Unknown state: " + status);
  }
}
