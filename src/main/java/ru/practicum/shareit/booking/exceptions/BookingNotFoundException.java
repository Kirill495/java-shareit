package ru.practicum.shareit.booking.exceptions;

public class BookingNotFoundException extends RuntimeException {

  public BookingNotFoundException(int id) {
    this(String.format("Бронирование с id %d не найдено", id));
  }

  public BookingNotFoundException(String message) {
    super(message);
  }
}
