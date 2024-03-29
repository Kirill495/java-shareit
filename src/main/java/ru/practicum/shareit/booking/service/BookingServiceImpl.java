package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.entity.BookingEntity;
import ru.practicum.shareit.booking.exceptions.BookingNotFoundException;
import ru.practicum.shareit.booking.exceptions.BookingSetStatusByOwnerException;
import ru.practicum.shareit.booking.exceptions.CreateBookingByOwnerException;
import ru.practicum.shareit.booking.exceptions.CreateBookingForUnavailableItemException;
import ru.practicum.shareit.booking.exceptions.UnknownBookingStateException;
import ru.practicum.shareit.booking.exceptions.UserIsNotOwnerOfBookedItem;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.item.exceptions.ItemNotFoundException;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.entity.UserEntity;
import ru.practicum.shareit.user.exceptions.UserNotFoundException;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

  private final UserRepository userRepository;
  private final ItemRepository itemRepository;
  private final BookingRepository bookingRepository;
  private final UserMapper userMapper;
  private final BookingMapper bookingMapper;
  private final ItemMapper itemMapper;

  @Override
  public Booking addBooking(int userId, int itemId, Booking inputBooking) {

    User booker = userMapper
            .toModel(userRepository
                    .findById(userId).orElseThrow(() -> new UserNotFoundException(userId)));
    inputBooking.setBooker(booker);
    Item item = itemMapper
            .toModel(itemRepository
                    .findById(itemId).orElseThrow(() -> new ItemNotFoundException(itemId)));
    if (!item.getAvailable()) {
      throw new CreateBookingForUnavailableItemException(item);
    } else if (item.getOwner().equals((booker))) {
      throw new CreateBookingByOwnerException(item.getId(), userId);
    }
    inputBooking.setBooker(booker);
    inputBooking.setItem(item);
    return bookingMapper
              .toModel(bookingRepository
                      .save(bookingMapper
                              .toEntity(inputBooking)));
  }

  @Override
  public Booking updateApprovedStatus(int userId, int bookingId, boolean approved) {
    User owner = userMapper.toModel(userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId)));
    Booking booking = bookingMapper.toModel(bookingRepository.getBookingWithAllPropertiesById(bookingId));
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
    return bookingMapper.toModel(bookingRepository.save(bookingMapper.toEntity(booking)));
  }

  @Override
  public Booking getBooking(int userId, int bookingId) {
    User user = userMapper.toModel(userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId)));
    Booking booking = bookingMapper.toModel(bookingRepository.getUserRelatedBookingById(
            userMapper.toEntity(user), bookingId));
    if (booking == null) {
      throw new BookingNotFoundException(bookingId);
    }
    return booking;
  }

  @Override
  public List<Booking> getBookingsOfItemOwner(int userId, String state, Integer from, Integer size) {
    UserEntity user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
    List<BookingEntity> bookings;
    Pageable pageable = PageRequest.of(from, size, Sort.by("start").descending());
    LocalDateTime now = LocalDateTime.now();
    switch (state.toUpperCase()) {
      case "ALL":
        bookings = bookingRepository.findByItemOwner(user, pageable).getContent();
        break;
      case "CURRENT":
        bookings = bookingRepository.findByItemOwnerAndStartBeforeAndEndAfter(user, now, now, pageable).getContent();
        break;
      case "PAST":
        bookings = bookingRepository.findByItemOwnerAndEndBefore(user, now, pageable).getContent();
        break;
      case "FUTURE":
        bookings = bookingRepository.findByItemOwnerAndStartAfter(user, now, pageable).getContent();
        break;
      case "WAITING":
        bookings = bookingRepository.findByItemOwnerAndStatus(user, BookingStatus.WAITING, pageable).getContent();
        break;
      case "REJECTED":
        bookings = bookingRepository.findByItemOwnerAndStatus(user, BookingStatus.REJECTED, pageable).getContent();
        break;
      default:
        throw new UnknownBookingStateException(state);
    }
    return bookingMapper.toModel(bookings);
  }

  @Override
  public List<Booking> getBookingsOfBooker(int userId, String state, Integer from, Integer size) {
    UserEntity user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
    Page<BookingEntity> bookingsPage;
    LocalDateTime now = LocalDateTime.now();
    Pageable page = PageRequest.of(from / size, size, Sort.by("Start").descending());
    switch (state.toUpperCase()) {
      case "ALL":
        bookingsPage = bookingRepository.findByBooker(user, page);
        break;
      case "CURRENT":
        bookingsPage = bookingRepository.findByBookerAndStartBeforeAndEndAfter(user, now, now, page);
        break;
      case "PAST":
        bookingsPage = bookingRepository.findByBookerAndEndBefore(user, now, page);
        break;
      case "FUTURE":
        bookingsPage = bookingRepository.findByBookerAndStartAfter(user, now, page);
        break;
      case "WAITING":
        bookingsPage = bookingRepository.findByBookerAndStatus(user, BookingStatus.WAITING, page);
        break;
      case "REJECTED":
        bookingsPage = bookingRepository.findByBookerAndStatus(user, BookingStatus.REJECTED, page);
        break;
      default:
        throw new UnknownBookingStateException(state);
    }

    return bookingMapper.toModel(bookingsPage.getContent());
  }

}
