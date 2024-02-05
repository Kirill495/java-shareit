package ru.practicum.shareit.booking;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.booking.dto.BookingState;
import ru.practicum.shareit.booking.dto.InputBookingRequest;
import ru.practicum.shareit.client.BaseClient;

import java.util.Map;

@Service
public class BookingClient extends BaseClient {

  private static final String API_PREFIX = "/bookings";

  @Autowired
  public BookingClient(@Value("${server.url}") String serverUrl, RestTemplateBuilder builder) {
    super(
            builder
                    .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                    .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                    .build()
    );
  }

  public ResponseEntity<Object> getItemOwnerBookings(int userId, BookingState state, Integer from, Integer size) {
    return getBookingsInner("owner", userId, state, from, size);
  }

  public ResponseEntity<Object> getBookings(int userId, BookingState state, Integer from, Integer size) {
    return getBookingsInner("", userId, state, from, size);
  }

  public ResponseEntity<Object> bookItem(int userId, InputBookingRequest requestDto) {
    return post("", userId, requestDto);
  }

  public ResponseEntity<Object> getBooking(int userId, int bookingId) {
    return get("/" + bookingId, userId);
  }

  public ResponseEntity<Object> updateStatus(int userId, int bookingId, boolean approved) {
    return patch("/{bookingId}?approved={approved}", userId, Map.of("bookingId", bookingId, "approved", approved), null);
  }

  private ResponseEntity<Object> getBookingsInner(String resource, int userId, BookingState state, Integer from, Integer size) {
    Map<String, Object> parameters = Map.of(
            "state", state.name(),
            "from", from,
            "size", size
    );
    String path = (resource.isBlank() ? "" : "/" + resource) + "?state={state}&from={from}&size={size}";
    return get(path, userId, parameters);
  }
}
