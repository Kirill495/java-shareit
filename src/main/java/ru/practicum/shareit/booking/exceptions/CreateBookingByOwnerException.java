package ru.practicum.shareit.booking.exceptions;

public class CreateBookingByOwnerException extends RuntimeException {
  public CreateBookingByOwnerException(int itemId, int userId) {
    super(String.format(
            "Не удалось создать бронирование. Пользователь %d является владельцем предмета %d", itemId, userId));
  }
}
