package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.request.dto.InputItemRequestDto;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;

@RequiredArgsConstructor
@Validated
@RestController
@RequestMapping(path = "/requests")
public class ItemRequestController {

  private final RequestClient requestClient;

  @PostMapping
  public ResponseEntity<Object> addRequest(@RequestHeader(value = "X-Sharer-User-Id") int userId,
                                                  @RequestBody @Valid InputItemRequestDto input) {
    input.setCreated(LocalDateTime.now());
    return requestClient.addRequest(userId, input);
  }

  @GetMapping
  public ResponseEntity<Object> getOwnRequests(@RequestHeader(value = "X-Sharer-User-Id") int userId) {
    return requestClient.getUserRequests(userId);
  }

  @GetMapping("/{requestId}")
  public ResponseEntity<Object> getRequest(@RequestHeader(value = "X-Sharer-User-Id") int userId,
                                           @Positive @PathVariable(name = "requestId") int requestId) {
    return requestClient.getRequest(requestId, userId);
  }

  @GetMapping("/all")
  public ResponseEntity<Object> getOtherUserRequests(
          @RequestHeader(value = "X-Sharer-User-Id") int userId,
          @RequestParam(name = "from", defaultValue = "0") @PositiveOrZero Integer from,
          @RequestParam(name = "size", defaultValue = "10") @Positive Integer size) {
    return requestClient.getOtherUserRequests(userId, from, size);
  }

}
