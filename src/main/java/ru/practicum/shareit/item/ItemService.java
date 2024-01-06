package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.BookingMapper;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.dto.ItemNearestBooking;
import ru.practicum.shareit.booking.dto.LightBookingDTO;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.comment.Comment;
import ru.practicum.shareit.comment.CommentMapper;
import ru.practicum.shareit.comment.CommentRepository;
import ru.practicum.shareit.comment.Dto.InputCommentDto;
import ru.practicum.shareit.comment.Dto.OutputCommentDto;
import ru.practicum.shareit.item.dto.InputNewItemDTO;
import ru.practicum.shareit.item.dto.InputUpdatedItemDTO;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.exceptions.ItemNotFoundException;
import ru.practicum.shareit.item.exceptions.UserItemNotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.exceptions.UserNotFoundException;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemService {

  private final UserRepository userRepository;
  private final ItemRepository itemRepository;
  private final BookingRepository bookingRepository;
  private final CommentRepository commentRepository;

  public ItemDto createNewItem(int userId, InputNewItemDTO itemDto) {
    Item item = ItemMapper.toItem(itemDto);
    User owner = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
    item.setOwner(owner);
    Item savedItem = itemRepository.save(item);

    return ItemMapper.toItemDto(savedItem);
  }

  public ItemDto updateItem(int userId, int itemId, InputUpdatedItemDTO inputItem) {

    User owner = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
    Item item = itemRepository.findByOwnerAndId(owner, itemId);

    if (item == null) {
      throw new UserItemNotFoundException(itemId, userId);
    }

    if (inputItem.getAvailable() != null) {
      item.setAvailable(inputItem.getAvailable());
    }
    if (inputItem.getName() != null) {
      item.setName(inputItem.getName());
    }
    if (inputItem.getDescription() != null) {
      item.setDescription(inputItem.getDescription());
    }

    return ItemMapper.toItemDto(itemRepository.save(item));
  }

  public ItemDto getItem(int userId, int itemId) {
    User currentUser = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
    Item item = itemRepository.findById(itemId).orElseThrow(() -> new ItemNotFoundException(itemId));
    ItemDto itemDto = ItemMapper.toItemDto(item);
    if (item.getOwner().equals(currentUser)) {
      PageRequest page = PageRequest.of(0, 1);
      Booking lastBooking = bookingRepository.getLastBooking(item, page);
      itemDto.setLastBooking(BookingMapper.toLightBookingDTO(lastBooking));
      Booking nextBooking = bookingRepository.getNextBooking(item, page);
      itemDto.setNextBooking(BookingMapper.toLightBookingDTO(nextBooking));
    }
    List<Comment> comments = commentRepository.findByItemOrderById(item);
    if (!comments.isEmpty()) {
      itemDto.setComments(comments.stream().map(CommentMapper::toCommentDto).collect(Collectors.toList()));
    } else {
      itemDto.setComments(Collections.emptyList());
    }
    return itemDto;
  }

  public List<ItemDto> getUserItems(int userId) {
    User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
    List<Item> items = itemRepository.findByOwnerOrderById(user);
    List<Integer> itemsId = items.stream().map(Item::getId).collect(Collectors.toList());
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
            .map(ItemMapper::toItemDto)
            .map(itemDto -> {
              itemDto.setLastBooking(lastBookings.get(itemDto.getId()));
              itemDto.setNextBooking(nextBookings.get(itemDto.getId()));
              return itemDto; })
            .collect(Collectors.toList());

  }

  public OutputCommentDto addComment(InputCommentDto inputCommentDto,
                                     Integer itemId,
                                     Integer userId) {
    User author = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
    Item item = itemRepository.findById(itemId).orElseThrow(() -> new ItemNotFoundException(itemId));
    List<Booking> bookings = bookingRepository.findByBookerAndItemAndEndBefore(author, item, LocalDateTime.now());
    if (bookings.isEmpty()) {
      throw new NoBookingsToItemException(String.format("У пользователя %d нет завершенных бронирований для предмета %d", userId, itemId));
    }
    Comment comment = CommentMapper.toComment(inputCommentDto);
    comment.setAuthor(author);
    comment.setItem(item);

    return CommentMapper.toCommentDto(commentRepository.save(comment));

  }

  public List<ItemDto> searchItems(String text) {
    if (text.isBlank()) {
      return Collections.emptyList();
    }
    return itemRepository
            .searchAvailableItems(text.toUpperCase()).stream()
            .map(ItemMapper::toItemDto)
            .collect(Collectors.toList());
  }

}
