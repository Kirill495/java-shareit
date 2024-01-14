package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.ItemNearestBooking;
import ru.practicum.shareit.booking.dto.LightBookingDTO;
import ru.practicum.shareit.booking.entity.BookingEntity;
import ru.practicum.shareit.booking.exceptions.NoBookingsToItemException;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.item.dto.comment.InputCommentDto;
import ru.practicum.shareit.item.dto.comment.OutputCommentDto;
import ru.practicum.shareit.item.dto.item.NewItemRequest;
import ru.practicum.shareit.item.dto.item.ItemResponse;
import ru.practicum.shareit.item.dto.item.UpdateItemRequest;
import ru.practicum.shareit.item.entity.CommentEntity;
import ru.practicum.shareit.item.entity.ItemEntity;
import ru.practicum.shareit.item.exceptions.ItemNotFoundException;
import ru.practicum.shareit.item.exceptions.UserItemNotFoundException;
import ru.practicum.shareit.item.mapper.CommentMapper;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.entity.UserEntity;
import ru.practicum.shareit.user.exceptions.UserNotFoundException;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

  private final UserRepository userRepository;
  private final ItemRepository itemRepository;
  private final BookingRepository bookingRepository;
  private final CommentRepository commentRepository;

  private final ItemMapper itemMapper;
  private final BookingMapper bookingMapper;
  private final UserMapper userMapper;
  private final CommentMapper commentMapper;

  @Override
  public ItemResponse createNewItem(int userId, NewItemRequest request) {
    Item item = itemMapper.toModel(request);
    User owner = userMapper
            .toModel(userRepository
                    .findById(userId).orElseThrow(() -> new UserNotFoundException(userId)));
    item.setOwner(owner);
    ItemEntity savedItem = itemRepository.save(itemMapper.toEntity(item));

    return itemMapper.toResponse(itemMapper.toModel(savedItem));
  }

  @Override
  public ItemResponse updateItem(int userId, int itemId, UpdateItemRequest request) {

    Item inputItem = itemMapper.toModel(request);
    UserEntity ownerEntity = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
    ItemEntity itemEntity = itemRepository.findByOwnerAndId(ownerEntity, itemId);

    if (itemEntity == null) {
      throw new UserItemNotFoundException(itemId, userId);
    }
    if (inputItem.getAvailable() != null) {
      itemEntity.setAvailable(inputItem.getAvailable());
    }
    if (inputItem.getName() != null) {
      itemEntity.setName(inputItem.getName());
    }
    if (inputItem.getDescription() != null) {
      itemEntity.setDescription(inputItem.getDescription());
    }

    return itemMapper.toResponse(itemMapper.toModel(itemRepository.save(itemEntity)));
  }

  @Override
  public ItemResponse getItem(int userId, int itemId) {
    User currentUser = userMapper.toModel(
            userRepository.findById(userId)
                    .orElseThrow(() -> new UserNotFoundException(userId)));
    Item item = itemMapper.toModel(
            itemRepository.findById(itemId)
                    .orElseThrow(() -> new ItemNotFoundException(itemId)));
    ItemResponse response = itemMapper.toResponse(item);

    ItemEntity itemEntity = itemMapper.toEntity(item);
    if (item.getOwner().equals(currentUser)) {
      PageRequest page = PageRequest.of(0, 1);
      BookingEntity lastBooking = bookingRepository.getLastBooking(itemEntity, page);
      BookingEntity nextBooking = bookingRepository.getNextBooking(itemEntity, page);
      itemMapper.appendLastBooking(
              response,
              bookingMapper.toModel(lastBooking));
      itemMapper.appendNextBooking(
              response,
              bookingMapper.toModel(nextBooking));
    }
    List<CommentEntity> comments = commentRepository.findByItemOrderById(itemEntity);

    if (!comments.isEmpty()) {
      response
              .setComments(comments.stream()
                      .map(comment -> new ItemResponse.Comment(comment.getId(), comment.getText(), comment.getAuthor().getName(), comment.getCreated().toString()))
                      .collect(Collectors.toList()));
    } else {
      response.setComments(Collections.emptyList());
    }
    return response;
  }

  @Override
  public List<ItemResponse> getUserItems(int userId) {
    UserEntity userEntity  = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
    List<ItemEntity> items = itemRepository.findByOwnerOrderById(userEntity);
    List<Integer> itemsId = items.stream().map(ItemEntity::getId).collect(Collectors.toList());
    List<ItemNearestBooking> lastBookingsRaw = bookingRepository.getLastBookings(itemsId);
    Map<Integer, LightBookingDTO> lastBookings = lastBookingsRaw.stream()
            .collect(Collectors.toMap(
                    ItemNearestBooking::getItemId,
                    b -> new LightBookingDTO(b.getBookingId(), b.getBookerId())));
    List<ItemNearestBooking> nextBookingsRaw = bookingRepository.getNextBookings(itemsId);
    Map<Integer, LightBookingDTO> nextBookings = nextBookingsRaw.stream()
            .collect(Collectors.toMap(
                    ItemNearestBooking::getItemId,
                    b -> new LightBookingDTO(b.getBookingId(), b.getBookerId())));
    return items.stream()
            .map(itemMapper::toModel)
            .map(itemMapper::toResponse)
            .peek(itemResponse -> {
              itemMapper.appendLastBooking(itemResponse, lastBookings.get(itemResponse.getId()));
              itemMapper.appendNextBooking(itemResponse, nextBookings.get(itemResponse.getId()));
            })
            .collect(Collectors.toList());

  }

  @Override
  public OutputCommentDto addComment(InputCommentDto inputCommentDto, Integer itemId, Integer userId) {
    UserEntity authorEntity = userRepository
            .findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
    ItemEntity item = itemRepository
            .findById(itemId).orElseThrow(() -> new ItemNotFoundException(itemId));
    List<BookingEntity> bookings = bookingRepository
            .findByBookerAndItemAndEndBefore(authorEntity, item, LocalDateTime.now());
    if (bookings.isEmpty()) {
      throw new NoBookingsToItemException(
              String.format("У пользователя %d нет завершенных бронирований для предмета %d", userId, itemId));
    }
    Comment comment = commentMapper.toModel(inputCommentDto);
    comment.setAuthor(userMapper.toModel(authorEntity));
    comment.setItem(itemMapper.toModel(item));
    return commentMapper.toDto(commentMapper.toModel(commentRepository.save(commentMapper.toEntity(comment))));
  }

  @Override
  public List<ItemResponse> searchItems(String text) {
    if (text.isBlank()) {
      return Collections.emptyList();
    }
    return itemRepository
            .searchAvailableItems(text.toUpperCase()).stream()
            .map(itemMapper::toModel)
            .map(itemMapper::toResponse)
            .collect(Collectors.toList());
  }

}
