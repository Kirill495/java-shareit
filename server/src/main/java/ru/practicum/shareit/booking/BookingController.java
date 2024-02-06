package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.booking.dto.BookingResponse;
import ru.practicum.shareit.booking.dto.InputBookingRequest;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.service.BookingService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/bookings")
public class BookingController {

  private final BookingService bookingService;
  private final BookingMapper mapper;

  @PostMapping
  public ResponseEntity<BookingResponse> createNewBooking(@RequestHeader(value = "X-Sharer-User-Id") int userId,
                                                         @RequestBody InputBookingRequest inputBooking) {
    return new ResponseEntity<>(
          mapper.toResponse(bookingService.addBooking(userId, inputBooking.getItemId(), mapper.toModel(inputBooking))),
          HttpStatus.CREATED);
  }

  @PatchMapping("/{bookingId}")
  public ResponseEntity<BookingResponse> updateApprovedStatus(@RequestHeader(value = "X-Sharer-User-Id") int userId,
                                              @PathVariable int bookingId,
                                              @RequestParam(value = "approved") boolean approved) {
    return new ResponseEntity<>(
            mapper.toResponse(bookingService.updateApprovedStatus(userId, bookingId, approved)),
            HttpStatus.OK);
  }

  @GetMapping(path = "/{bookingId}")
  public ResponseEntity<BookingResponse> getBooking(@RequestHeader(value = "X-Sharer-User-Id") int userId,
                                    @PathVariable(value = "bookingId") int bookingId) {
    return new ResponseEntity<>(
            mapper.toResponse(bookingService.getBooking(userId, bookingId)),
            HttpStatus.OK);
  }

  @GetMapping(path = "/owner")
  public ResponseEntity<List<BookingResponse>> getBookings(@RequestHeader(value = "X-Sharer-User-Id") int userId,
                                           @RequestParam(value = "state") String state,
                                           @RequestParam(value = "from") Integer from,
                                           @RequestParam(value = "size") Integer size) {
    return new ResponseEntity<>(
            mapper.toResponse(bookingService.getBookingsOfItemOwner(userId, state, from, size)),
            HttpStatus.OK);
  }

  @GetMapping
  public ResponseEntity<List<BookingResponse>> getBookingsByBooker(@RequestHeader(value = "X-Sharer-User-Id") int userId,
                                                   @RequestParam(value = "state") String state,
                                                   @RequestParam(value = "from") Integer from,
                                                   @RequestParam(value = "size") Integer size) {
    return new ResponseEntity<>(
            mapper.toResponse(bookingService.getBookingsOfBooker(userId, state, from, size)),
            HttpStatus.OK);
  }

}