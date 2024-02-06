package ru.practicum.shareit.booking.exceptions;

public class UserIsNotOwnerOfBookedItem extends RuntimeException {
  public UserIsNotOwnerOfBookedItem(int userId, int bookingId) {
    this(String.format("Пользователь %d не является владельцем предмета в бронировании %d", userId, bookingId));
  }

  public UserIsNotOwnerOfBookedItem(String message) {
    super(message);
  }
}
