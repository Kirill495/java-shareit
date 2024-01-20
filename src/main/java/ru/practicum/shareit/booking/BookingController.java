package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
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
import ru.practicum.shareit.booking.dto.BookingResponse;
import ru.practicum.shareit.booking.dto.InputBookingRequest;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.service.BookingService;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/bookings")
public class BookingController {

  private final BookingService bookingService;
  private final BookingMapper mapper;

  @PostMapping
  public BookingResponse createNewBooking(@RequestHeader(value = "X-Sharer-User-Id") int userId,
                                          @RequestBody @Valid InputBookingRequest inputBooking) {
    return mapper
            .toResponse(bookingService
                    .addBooking(userId, inputBooking.getItemId(), mapper.toModel(inputBooking)));
  }

  @PatchMapping("/{bookingId}")
  public BookingResponse updateApprovedStatus(@RequestHeader(value = "X-Sharer-User-Id") int userId,
                                              @PathVariable int bookingId,
                                              @RequestParam(value = "approved") boolean approved) {
    return mapper.toResponse(bookingService.updateApprovedStatus(userId, bookingId, approved));
  }

  @GetMapping(path = "/{bookingId}")
  public BookingResponse getBooking(@RequestHeader(value = "X-Sharer-User-Id") int userId,
                                    @PathVariable(value = "bookingId") int bookingId) {
    return mapper.toResponse(bookingService.getBooking(userId, bookingId));
  }

  @GetMapping(path = "/owner")
  public List<BookingResponse> getBookings(@RequestHeader(value = "X-Sharer-User-Id") int userId,
                                           @RequestParam(value = "state", defaultValue = "ALL") String state,
                                           @Valid @RequestParam(value = "from", defaultValue = "0") @Min(value = 0L) Integer from,
                                           @Valid @RequestParam(value = "size", defaultValue = "100") @Min(value = 1L) Integer size) {
    return mapper.toResponse(bookingService.getBookingsOfItemOwner(userId, state, from, size));
  }

  @GetMapping
  public List<BookingResponse> getBookingsByBooker(@RequestHeader(value = "X-Sharer-User-Id") int userId,
                                                   @RequestParam(value = "state", defaultValue = "ALL") String state,
                                                   @Valid @RequestParam(value = "from", defaultValue = "0") @Min(value = 0L) Integer from,
                                                   @Valid @RequestParam(value = "size", defaultValue = "100") @Min(value = 1L) Integer size) {
    return mapper.toResponse(bookingService.getBookingsOfBooker(userId, state, from, size));
  }


}
