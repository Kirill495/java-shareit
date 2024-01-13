package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.model.Booking;

import java.util.List;

public interface BookingService {
  Booking addBooking(int userId, int itemId, Booking inputBooking);

  Booking getBooking(int userId, int bookingId);

  List<Booking> getBookingsOfBooker(int userId, String state);

  List<Booking> getBookingsOfItemOwner(int userId, String state);

  Booking updateApprovedStatus(int userId, int bookingId, boolean approved);
}
