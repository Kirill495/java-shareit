package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.InputBooking;
import ru.practicum.shareit.booking.dto.LightBookingDTO;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.dto.LightItemDto;
import ru.practicum.shareit.user.UserMapper;

import java.util.ArrayList;
import java.util.List;

public class BookingMapper {
  public static BookingDto toBookingDto(Booking booking) {
    if (booking == null) {
      return null;
    }
    return BookingDto.builder()
            .withId(booking.getId())
            .withStart(booking.getStart())
            .withEnd(booking.getEnd())
            .withStatus(booking.getStatus())
            .withBooker(UserMapper.toLightUserDto(booking.getBooker()))
            .withItem(LightItemDto.toLightItem(booking.getItem()))
            .build();
  }

  public static List<BookingDto> toBookingDtos(Iterable<Booking> bookings) {
    List<BookingDto> result = new ArrayList<>();
    for (Booking booking: bookings) {
      result.add(toBookingDto(booking));
    }
    return result;
  }

  public static Booking toBooking(InputBooking inputBooking) {
    return Booking.builder()
            .withStart(inputBooking.getStart())
            .withEnd(inputBooking.getEnd())
            .withStatus(BookingStatus.WAITING)
            .build();
  }

  public static LightBookingDTO toLightBookingDTO(Booking booking) {
    if (booking == null) {
      return null;
    }
    return LightBookingDTO.builder()
            .withId(booking.getId())
            .withBookerId(booking.getBooker().getId())
            .build();
  }
}
