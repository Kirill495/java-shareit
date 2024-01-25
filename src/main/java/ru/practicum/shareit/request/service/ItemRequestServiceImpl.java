package ru.practicum.shareit.request.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.entity.ItemEntity;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.entity.ItemRequestEntity;
import ru.practicum.shareit.request.exceptions.GetRequestsParameterException;
import ru.practicum.shareit.request.exceptions.ItemRequestNotFoundException;
import ru.practicum.shareit.request.mapper.ItemRequestMapper;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.entity.UserEntity;
import ru.practicum.shareit.user.exceptions.UserNotFoundException;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ItemRequestServiceImpl implements ItemRequestService {

  private final UserRepository userRepository;
  private final ItemRepository itemRepository;
  private final ItemRequestRepository requestRepository;

  private final UserMapper userMapper;
  private final ItemMapper itemMapper;
  private final ItemRequestMapper requestMapper;

  @Override
  public ItemRequest addRequest(int userId, ItemRequest request) {
    UserEntity userEntity = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
    User user = userMapper.toModel(userEntity);
    request.setAuthor(user);

    ItemRequestEntity savedRequest = requestRepository.save(requestMapper.toEntity(request));
    return requestMapper.toModel(savedRequest);
  }

  @Override
  public Collection<ItemRequestDto> getUserRequests(int userId) {
    UserEntity userEntity = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
    Collection<ItemRequestEntity> requests = requestRepository.findByAuthorOrderByCreated(userEntity);

    return convertToItemRequestDtos(requests);
  }

  @Override
  public ItemRequestDto getRequest(int userId, int requestId) {
    if (!userRepository.existsById(userId)) {
      throw new UserNotFoundException(userId);
    }

    ItemRequestEntity entity = requestRepository
            .findById(requestId).orElseThrow(() -> new ItemRequestNotFoundException(requestId));
    ItemRequestDto requestDto = requestMapper.toItemRequestDto(requestMapper.toModel(entity));
    Collection<ItemEntity> itemEntities = itemRepository.findByRequestInOrderById(List.of(entity));
    if (!itemEntities.isEmpty()) {
      Collection<ItemRequestDto.Item> items = requestMapper.toItemDto(itemMapper.toModel(itemEntities));
      for (ItemRequestDto.Item item : items) {
        item.setRequestId(requestId);
      }
      requestDto.setItems(items);
    }
    return requestDto;
  }

  @Override
  public Collection<ItemRequestDto> getRequests(int userId, Integer from, Integer size) {
    UserEntity userEntity = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
    if (from != null && from == 0 && size != null && size == 0) {
      throw new GetRequestsParameterException("Параметры from и size не могут быть оба равны нулю");
    }
    if (from == null) {
      from = 0;
    }
    if (size == null) {
      size = 0;
    }
    if (size == 0) {
      return Collections.emptyList();
    }
    PageRequest page = PageRequest.of(from / size, size).withSort(Sort.by("created").descending());
    Collection<ItemRequestEntity> requestEntities = requestRepository.findByAuthorNot(userEntity, page);

    return convertToItemRequestDtos(requestEntities);
  }

  private Collection<ItemRequestDto> convertToItemRequestDtos(Collection<ItemRequestEntity> requests) {

    Map<Integer, List<Item>> items = itemMapper.toModel(itemRepository.findByRequestInOrderById(requests)).stream()
            .collect(Collectors.groupingBy(item -> item.getRequest().getId(), Collectors.toList()));
    Collection<ItemRequestDto> requestDtos = requestMapper.toItemRequestDto(requestMapper.toModel(requests));

    for (ItemRequestDto requestDto: requestDtos) {
      List<Item> requestItems = items.get(requestDto.getId());
      if (requestItems == null) {
        requestDto.setItems(Collections.emptyList());
      } else {
        Collection<ItemRequestDto.Item> itemRequestDtos = requestMapper.toItemDto(requestItems);
        for (ItemRequestDto.Item item : itemRequestDtos) {
          item.setRequestId(requestDto.getId());
        }
        requestDto.setItems(itemRequestDtos);
      }
    }
    return requestDtos;
  }
}
