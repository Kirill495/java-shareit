package ru.practicum.shareit.booking.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.BookingMapper;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.InputBooking;
import ru.practicum.shareit.booking.exceptions.BookingNotFoundException;
import ru.practicum.shareit.booking.exceptions.BookingSetStatusByOwnerException;
import ru.practicum.shareit.booking.exceptions.CreateBookingByOwnerException;
import ru.practicum.shareit.booking.exceptions.CreateBookingForUnavailableItemException;
import ru.practicum.shareit.booking.exceptions.UnknownBookingStateException;
import ru.practicum.shareit.booking.exceptions.UserIsNotOwnerOfBookedItem;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.exceptions.ItemNotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.exceptions.UserNotFoundException;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class BookingServiceImpl implements BookingService {

  @Autowired
  UserRepository userRepository;
  @Autowired
  ItemRepository itemRepository;
  @Autowired
  BookingRepository bookingRepository;

  @Override
  public BookingDto addBooking(int userId, InputBooking inputBooking) {

    User booker = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
    int itemId = inputBooking.getItemId();
    Item item = itemRepository.findById(itemId).orElseThrow(() -> new ItemNotFoundException(itemId));
    if (!item.getAvailable()) {
      throw new CreateBookingForUnavailableItemException(item);
    } else if (item.getOwner().equals((booker))) {
      throw new CreateBookingByOwnerException(item.getId(), userId);
    }
    Booking booking = BookingMapper.toBooking(inputBooking);
    booking.setBooker(booker);
    booking.setItem(item);
    Booking savedBooking = bookingRepository.save(booking);

    return BookingMapper.toBookingDto(savedBooking);
  }

  @Override
  public BookingDto updateApprovedStatus(int userId, int bookingId, boolean approved) {
    User owner = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
    Booking booking = bookingRepository.getBookingWithAllPropertiesById(bookingId);
    if (booking == null) {
      throw new BookingNotFoundException(bookingId);
    }
    if (!booking.getItem().getOwner().equals(owner)) {
      throw new UserIsNotOwnerOfBookedItem(owner.getId(), bookingId);
    }
    if (booking.getStatus() == BookingStatus.WAITING) {
      if (approved) {
        booking.setStatus(BookingStatus.APPROVED);
      } else {
        booking.setStatus(BookingStatus.REJECTED);
      }
    } else {
      throw new BookingSetStatusByOwnerException(String.format("Не удалось обновить статус бронирования. " +
              "Исходный статус бронирования: %s, а должен быть WAITING", booking.getStatus().toString()));
    }
    return BookingMapper.toBookingDto(bookingRepository.save(booking));
  }

  @Override
  public BookingDto getBooking(int userId, int bookingId) {
    User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
    Booking booking = bookingRepository.getUserRelatedBookingById(user, bookingId);
    if (booking == null) {
      throw new BookingNotFoundException(bookingId);
    }
    return BookingMapper.toBookingDto(booking);
  }

  @Override
  public List<BookingDto> getBookingsOfItemOwner(int userId, String state) {
    User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
    List<Booking> bookings;

    switch (state.toUpperCase()) {
      case "ALL":
        bookings = bookingRepository.getAllByItemOwner(user);
        break;
      case "CURRENT":
        bookings = bookingRepository.getCurrentByItemOwner(user);
        break;
      case "PAST":
        bookings = bookingRepository.getPastByItemOwner(user);
        break;
      case "FUTURE":
        bookings = bookingRepository.getFutureByItemOwner(user);
        break;
      case "WAITING":
        bookings = bookingRepository.getByItemOwnerAndStatus(user, BookingStatus.WAITING);
        break;
      case "REJECTED":
        bookings = bookingRepository.getByItemOwnerAndStatus(user, BookingStatus.REJECTED);
        break;
      default:
        throw new UnknownBookingStateException(state);
    }
    return BookingMapper.toBookingDtos(bookings);

  }

  @Override
  public List<BookingDto> getBookingsOfBooker(int userId, String state) {
    User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
    List<Booking> bookings;

    LocalDateTime now = LocalDateTime.now();
    switch (state.toUpperCase()) {
      case "ALL":
        bookings = bookingRepository.findByBookerOrderByStartDesc(user);
        break;
      case "CURRENT":
        bookings = bookingRepository.findByBookerAndStartBeforeAndEndAfterOrderByEndDescId(user, now, now);
        break;
      case "PAST":
        bookings = bookingRepository.findByBookerAndEndBeforeOrderByStartDesc(user, now);
        break;
      case "FUTURE":
        bookings = bookingRepository.findByBookerAndStartAfterOrderByStartDesc(user, now);
        break;
      case "WAITING":
        bookings = bookingRepository.findByBookerAndStatusOrderByStartDesc(user, BookingStatus.WAITING);
        break;
      case "REJECTED":
        bookings = bookingRepository.findByBookerAndStatusOrderByStartDesc(user, BookingStatus.REJECTED);
        break;
      default:
        throw new UnknownBookingStateException(state);
    }
    return BookingMapper.toBookingDtos(bookings);
  }

}
