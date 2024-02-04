package ru.practicum.shareit.booking.exceptions;

import ru.practicum.shareit.item.model.Item;

public class CreateBookingForUnavailableItemException extends RuntimeException {
  public CreateBookingForUnavailableItemException(String message) {
    super(message);
  }

  public CreateBookingForUnavailableItemException(Item item) {
    this(String.format("Item '%s' (%d) is unavailable", item.getName(), item.getId()));
  }
}
