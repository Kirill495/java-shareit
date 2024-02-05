package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.booking.dto.BookingState;
import ru.practicum.shareit.booking.dto.InputBookingRequest;
import ru.practicum.shareit.booking.exceptions.CreateNewBookingException;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/bookings")
public class BookingController {

  private final BookingClient bookingClient;

  @PostMapping
  public ResponseEntity<Object> createNewBooking(@RequestHeader(value = "X-Sharer-User-Id") int userId,
                                          @RequestBody @Valid InputBookingRequest inputBooking) {
    if (inputBooking.getEnd().isAfter(inputBooking.getStart())) {
      return bookingClient.bookItem(userId, inputBooking);
    } else {
      throw new CreateNewBookingException("дата начала должна быть меньше даты окончания");
    }
  }

  @PatchMapping("/{bookingId}")
  public ResponseEntity<Object> updateApprovedStatus(@RequestHeader(value = "X-Sharer-User-Id") int userId,
                                              @Positive @PathVariable int bookingId,
                                              @RequestParam(value = "approved") boolean approved) {
    return bookingClient.updateStatus(userId, bookingId, approved);
  }

  @GetMapping(path = "/{bookingId}")
  public ResponseEntity<Object> getBooking(@RequestHeader(value = "X-Sharer-User-Id") int userId,
                                           @Positive @PathVariable(value = "bookingId") int bookingId) {
    return bookingClient.getBooking(userId, bookingId);
  }

  @GetMapping(path = "/owner")
  public ResponseEntity<Object> getBookings(@RequestHeader(value = "X-Sharer-User-Id") int userId,
                                           @RequestParam(value = "state", defaultValue = "ALL") String stateParam,
                                           @PositiveOrZero @RequestParam(value = "from", defaultValue = "0") Integer from,
                                           @Positive @RequestParam(value = "size", defaultValue = "10") Integer size) {
    BookingState state = BookingState.from(stateParam).orElseThrow(() -> new IllegalArgumentException("Unknown state: " + stateParam));
    return bookingClient.getItemOwnerBookings(userId, state, from, size);
  }

  @GetMapping
  public ResponseEntity<Object> getBookingsByBooker(@RequestHeader(value = "X-Sharer-User-Id") int userId,
                                                   @RequestParam(value = "state", defaultValue = "ALL") String stateParam,
                                                   @PositiveOrZero @RequestParam(value = "from", defaultValue = "0") Integer from,
                                                   @Positive @RequestParam(value = "size", defaultValue = "10") Integer size) {
    BookingState state = BookingState.from(stateParam).orElseThrow(() -> new IllegalArgumentException("Unknown state:" + stateParam));
    return bookingClient.getBookings(userId, state, from, size);
  }


}
