package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.InputBooking;

import java.util.List;

public interface BookingService {
  BookingDto addBooking(int userId, InputBooking inputBooking);

  BookingDto getBooking(int userId, int bookingId);

  List<BookingDto> getBookingsOfBooker(int userId, String state);

  List<BookingDto> getBookingsOfItemOwner(int userId, String state);

  BookingDto updateApprovedStatus(int userId, int bookingId, boolean approved);
}
