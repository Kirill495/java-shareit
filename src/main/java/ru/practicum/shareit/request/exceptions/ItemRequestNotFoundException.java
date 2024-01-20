package ru.practicum.shareit.request.exceptions;

public class ItemRequestNotFoundException extends RuntimeException {
  public ItemRequestNotFoundException(int requestId) {
    this("Item request with id " + requestId + " is not found.");
  }

  public ItemRequestNotFoundException(String message) {
    super(message);
  }
}
