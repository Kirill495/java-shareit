package ru.practicum.shareit.request.service;

import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;

import java.util.Collection;

public interface ItemRequestService {
  ItemRequest addRequest(int userId, ItemRequest request);

  Collection<ItemRequestDto> getUserRequests(int userId);

  ItemRequestDto getRequest(int userId, int requestId);

  Collection<ItemRequestDto> getRequests(int userId, Integer from, Integer size);
}
