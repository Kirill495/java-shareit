package ru.practicum.shareit.request.controller;

import lombok.RequiredArgsConstructor;
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
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.mapper.ItemRequestMapper;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.service.ItemRequestService;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.Collection;

@RequiredArgsConstructor
@Validated
@RestController
@RequestMapping(path = "/requests")
public class ItemRequestController {

  private final ItemRequestMapper mapper;
  private final ItemRequestService service;

  @PostMapping
  public ItemRequestDto addRequest(@RequestHeader(value = "X-Sharer-User-Id") int userId,
                                   @RequestBody @Valid InputItemRequestDto input) {
    ItemRequest request = mapper.toModel(input);
    ItemRequest newRequest = service.addRequest(userId, request);
    return mapper.toItemRequestDto(newRequest);
  }

  @GetMapping
  public Collection<ItemRequestDto> getOwnRequests(@RequestHeader(value = "X-Sharer-User-Id") int userId) {
    return service.getUserRequests(userId);
  }

  @GetMapping("/{requestId}")
  public ItemRequestDto getRequest(@RequestHeader(value = "X-Sharer-User-Id") int userId,
                                   @PathVariable(name = "requestId") int requestId) {
    return service.getRequest(userId, requestId);
  }

  @GetMapping("/all")
  public Collection<ItemRequestDto> getOtherUserRequests(
          @RequestHeader(value = "X-Sharer-User-Id") int userId,
          @RequestParam(name = "from", required = false) @Min(value = 0L) Integer from,
          @RequestParam(name = "size", required = false) @Min(value = 0L) Integer size) {
    return service.getRequests(userId, from, size);
  }

}
