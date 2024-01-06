package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.InputBooking;
import ru.practicum.shareit.booking.service.BookingService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/bookings")
public class BookingController {

  private final BookingService bookingService;

  @PostMapping
  public BookingDto createNewBooking(@RequestHeader(value = "X-Sharer-User-Id") int userId,
                                     @RequestBody @Valid InputBooking inputBooking) {
    return bookingService.addBooking(userId, inputBooking);
  }

  @PatchMapping("/{bookingId}")
  public BookingDto updateApprovedStatus(@RequestHeader(value = "X-Sharer-User-Id") int userId,
                                         @PathVariable int bookingId,
                                         @RequestParam(value = "approved") boolean approved) {
    return bookingService.updateApprovedStatus(userId, bookingId, approved);
  }

  @GetMapping(path = "/{bookingId}")
  public BookingDto getBooking(@RequestHeader(value = "X-Sharer-User-Id") int userId,
                               @PathVariable(value = "bookingId") int bookingId) {
    return bookingService.getBooking(userId, bookingId);
  }

  @GetMapping(path = "/owner")
  public List<BookingDto> getBookings(@RequestHeader(value = "X-Sharer-User-Id") int userId,
                                      @RequestParam(value = "state", defaultValue = "ALL") String state) {
    return bookingService.getBookingsOfItemOwner(userId, state);
  }

  @GetMapping(path = "")
  public List<BookingDto> getBookingsByBooker(@RequestHeader(value = "X-Sharer-User-Id") int userId,
                                             @RequestParam(value = "state", defaultValue = "ALL") String state) {
    return bookingService.getBookingsOfBooker(userId, state);
  }


}
