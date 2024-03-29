package ru.practicum.shareit.item.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;
import ru.practicum.shareit.booking.entity.BookingEntity;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.item.dto.comment.InputCommentDto;
import ru.practicum.shareit.item.dto.comment.OutputCommentDto;
import ru.practicum.shareit.item.dto.item.ItemResponse;
import ru.practicum.shareit.item.dto.item.NewItemRequest;
import ru.practicum.shareit.item.entity.CommentEntity;
import ru.practicum.shareit.item.entity.ItemEntity;
import ru.practicum.shareit.item.exceptions.ItemNotFoundException;
import ru.practicum.shareit.item.mapper.CommentMapper;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.entity.ItemRequestEntity;
import ru.practicum.shareit.request.exceptions.ItemRequestNotFoundException;
import ru.practicum.shareit.request.mapper.ItemRequestMapper;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.entity.UserEntity;
import ru.practicum.shareit.user.exceptions.UserNotFoundException;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyInt;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles(profiles = {"test"})
class ItemServiceTest {

  @Mock
  UserMapper userMapper;
  @Mock
  ItemMapper itemMapper;
  @Mock
  ItemRepository itemRepository;
  @Mock
  UserRepository userRepository;
  @Mock
  BookingRepository bookingRepository;
  @Mock
  CommentRepository commentRepository;
  @Mock
  ItemRequestMapper requestMapper;
  @Mock
  ItemRequestRepository requestRepository;
  @Mock
  CommentMapper commentMapper;
  @Mock
  BookingMapper bookingMapper;

  @InjectMocks
  ItemServiceImpl itemService;

  @Test
  void getItem_whenItemIsFound_thenReturnItem() {
    int userId = 2;
    int itemId = 1;
    UserEntity userEntity = new UserEntity(userId, "Semen", "semen@email.ru");
    User user = new User(userId, "Semen", "semen@email.ru");

    ItemResponse control = new ItemResponse(1, new ItemResponse.User(1), "chair", "wood chair", true, null, null, null, Collections.emptyList());
    Item itemModel = Item.builder().withId(itemId).withName("chair").withDescription("wood chair").withOwner(user).build();
    ItemEntity itemEntity = ItemEntity.builder().withId(itemId).withName("chair").withDescription("wood chair").build();

    Mockito.when(userRepository.findById(userId)).thenReturn(Optional.of(userEntity));
    Mockito.when(userMapper.toModel(userEntity)).thenReturn(user);
    Mockito.when(itemRepository.findById(itemId)).thenReturn(Optional.of(itemEntity));
    Mockito.when(itemMapper.toModel(itemEntity)).thenReturn(itemModel);
    Mockito.when(itemMapper.toResponse(itemModel)).thenReturn(control);

    ItemResponse itemResponse = itemService.getItem(userId, itemId);

    assertEquals(control, itemResponse);

  }

  @Test
  void getItem_whenUserIsNotFound_thenThrowUserNotFoundException() {
    int userId = 0;
    int itemId = 1;
    Mockito.when(userRepository.findById(userId)).thenReturn(Optional.empty());

    assertThrows(UserNotFoundException.class, () -> itemService.getItem(userId, itemId));

    Mockito.verify(userRepository, Mockito.times(1)).findById(userId);
  }

  @Test
  void getItem_whenItemIsNotFound_thenThrowItemNotFoundException() {
    int userId = 1;
    int itemId = 0;
    Mockito.when(userRepository.findById(userId)).thenReturn(Optional.of(new UserEntity(userId, "Vlad", "vlad@emael.org")));
    Mockito.when(itemRepository.findById(itemId)).thenThrow(ItemNotFoundException.class);
    assertThrows(ItemNotFoundException.class, () -> itemService.getItem(userId, itemId));

    Mockito.verify(userRepository, Mockito.times(1)).findById(userId);
    Mockito.verify(itemRepository, Mockito.times(1)).findById(itemId);
  }

  @Test
  void createNewItem_whenInputIsCorrect_thenReturnNewItem() {
    int itemId = 1;
    int userId = 1;
    int requestId = 1;
    UserEntity userEntity = new UserEntity(userId, "Maxim", "maxim@email.org");
    User user = new User(userId, "Maxim", "maxim@email.org");
    NewItemRequest itemRequest = new NewItemRequest("table", "plastic table", true, requestId);
    Item itemModel = new Item(null, user, "table", "plastic table", true, null);
    ItemEntity itemEntity = new ItemEntity(null, userEntity, "table", "plastic table", true, null);

    ItemRequestEntity requestEntity = new ItemRequestEntity(requestId, "request1", null, null);
    ItemRequest request = new ItemRequest(requestId, "request1", null, null);

    ItemEntity itemEntityToSave = new ItemEntity(null, userEntity, "table", "plastic table", true, requestEntity);
    ItemEntity savedItemEntity = new ItemEntity(itemId, userEntity, "table", "plastic table", true, requestEntity);
    Item savedItemModel = new Item(itemId, user, "table", "plastic table", true, request);
    ItemResponse itemResponse = new ItemResponse(itemId, new ItemResponse.User(userId),
            "table", "plastic table", true, null, null, requestId, null);

    Mockito.when(itemMapper.toModel(itemRequest)).thenReturn(itemModel);
    Mockito.when(userRepository.findById(userId)).thenReturn(Optional.of(userEntity));
    Mockito.when(userMapper.toModel(userEntity)).thenReturn(user);
    Mockito.when(itemMapper.toEntity(itemModel)).thenReturn(itemEntity);
    Mockito.when(requestRepository.findById(requestId)).thenReturn(Optional.of(requestEntity));
    Mockito.when(requestMapper.toModel(requestEntity)).thenReturn(request);
    Mockito.when(itemRepository.save(itemEntityToSave)).thenReturn(savedItemEntity);
    Mockito.when(itemMapper.toModel((savedItemEntity))).thenReturn(savedItemModel);
    Mockito.when(itemMapper.toResponse(savedItemModel)).thenReturn(itemResponse);

    itemService.createNewItem(userId, itemRequest);

    Mockito.verify(userRepository, Mockito.times(1)).findById(anyInt());
    Mockito.verify(itemMapper, Mockito.times(1)).toModel(itemRequest);
    Mockito.verify(itemMapper, Mockito.times(1)).toEntity((itemModel));
    Mockito.verify(itemRepository, Mockito.times(1)).save(itemEntity);
    Mockito.verify(itemMapper, Mockito.times(1)).toModel(savedItemEntity);
    Mockito.verify(itemMapper, Mockito.times(1)).toResponse(savedItemModel);
  }

  @Test
  void createNewItem_whenRequestIdNotCorrect_thenThrowItemRequestNotFoundException() {
    int userId = 1;
    int requestId = 1;
    UserEntity userEntity = new UserEntity(userId, "Maxim", "maxim@email.org");
    User user = new User(userId, "Maxim", "maxim@email.org");
    NewItemRequest itemRequest = new NewItemRequest("table", "plastic table", true, requestId);
    Item itemModel = new Item(null, user, "table", "plastic table", true, null);
    ItemEntity itemEntity = new ItemEntity(null, userEntity, "table", "plastic table", true, null);

    Mockito.when(itemMapper.toModel(itemRequest)).thenReturn(itemModel);
    Mockito.when(userRepository.findById(userId)).thenReturn(Optional.of(userEntity));
    Mockito.when(userMapper.toModel(userEntity)).thenReturn(user);
    Mockito.when(requestRepository.findById(requestId)).thenReturn(Optional.empty());

    assertThrows(ItemRequestNotFoundException.class, () -> itemService.createNewItem(userId, itemRequest));

    Mockito.verify(userRepository, Mockito.times(1)).findById(anyInt());
    Mockito.verify(itemMapper, Mockito.times(1)).toModel(itemRequest);
    Mockito.verify(itemMapper, Mockito.never()).toEntity((itemModel));
    Mockito.verify(itemRepository, Mockito.never()).save(itemEntity);
  }

  @Test
  void createNewItem_whenUserIdNotCorrect_thenThrowUserNotFoundException() {
    int userId = 1;
    int requestId = 1;
    NewItemRequest itemRequest = new NewItemRequest("table", "plastic table", true, requestId);

    Mockito.when(userRepository.findById(userId)).thenReturn(Optional.empty());

    assertThrows(UserNotFoundException.class, () -> itemService.createNewItem(userId, itemRequest));

    Mockito.verify(userRepository, Mockito.times(1)).findById(anyInt());
    Mockito.verify(itemMapper, Mockito.times(1)).toModel(Mockito.any(NewItemRequest.class));
    Mockito.verify(itemMapper, Mockito.never()).toEntity(Mockito.any(Item.class));
  }

  @Test
  void searchItems_whenStringIsBlank_thenReturnEmptyList() {
    List<ItemResponse> items = itemService.searchItems("");
    assertTrue(items.isEmpty());
  }

  @Test
  void searchItems_whenNothingFound_thenReturnEmptyList() {
    String searchString = "1111";
    Mockito.when(itemRepository.searchAvailableItems(searchString)).thenReturn(Collections.emptyList());
    List<ItemResponse> items = itemService.searchItems(searchString);
    assertTrue(items.isEmpty());
  }

  @Test
  void searchItems_whenFoundOneItem_thenReturnListOfOneItem() {
    String searchString = "1111";
    ItemEntity itemEntity = new ItemEntity(1, null, searchString, searchString, true, null);
    Item itemModel = new Item(1, null, searchString, searchString, true, null);
    ItemResponse itemResponse = new ItemResponse(1, null, searchString, searchString, true, null, null, null, null);
    Mockito.when(itemMapper.toModel(itemEntity)).thenReturn(itemModel);
    Mockito.when(itemMapper.toResponse(itemModel)).thenReturn(itemResponse);
    Mockito.when(itemRepository.searchAvailableItems(searchString)).thenReturn(List.of(itemEntity));

    List<ItemResponse> items = itemService.searchItems(searchString);
    assertEquals(1, items.size());
    assertEquals(itemResponse, items.get(0));
  }

  @Test
  void addComment_whenInputCorrect_thenReturnNewComment() {
    int itemId = 1;
    int userId = 1;
    int requestId = 1;
    int bookingId = 1;
    int commentId = 1;

    UserEntity userEntity = new UserEntity(userId, "German", "german@email.org");
    User user = new User(userId, "German", "german@email.org");
    ItemRequestEntity requestEntity = new ItemRequestEntity(requestId, "demand for fork", null, userEntity);
    ItemRequest request = new ItemRequest(requestId, "demand for fork", null, user);
    ItemEntity itemEntity = new ItemEntity(itemId, null, "fork", "silver fork", true, requestEntity);
    Item item = new Item(itemId, null, "fork", "silver fork", true, request);
    LocalDateTime commentAddMoment = LocalDateTime.of(2024, 1, 21, 10, 0);
    BookingEntity bookingEntity = new BookingEntity(
            bookingId, userEntity, itemEntity,
            commentAddMoment.minus(2, ChronoUnit.HOURS),
            commentAddMoment.minus(1, ChronoUnit.HOURS),
            BookingStatus.APPROVED, null);
    List<BookingEntity> bookings = List.of(bookingEntity);
    InputCommentDto inputCommentDto = new InputCommentDto();
    inputCommentDto.setText("usual fork");
    Comment commentWithoutItemAndAuthor = new Comment(null, "usual fork", null, null, null);
    CommentEntity commentEntityInput = new CommentEntity(null, "usual fork", itemEntity, userEntity, null);
    CommentEntity commentEntitySaved = new CommentEntity(commentId, "usual fork", itemEntity, userEntity, null);
    Comment commentSaved = new Comment(commentId, "usual fork", item, user, commentAddMoment);
    OutputCommentDto commentDto = new OutputCommentDto(commentId, "usual fork", "German", "");
    Mockito.when(userRepository.findById(userId)).thenReturn(Optional.of(userEntity));
    Mockito.when(itemRepository.findById(itemId)).thenReturn(Optional.of(itemEntity));
    Mockito.when(bookingRepository.findByBookerAndItemAndEndBefore(Mockito.eq(userEntity), Mockito.eq(itemEntity), Mockito.any(LocalDateTime.class)))
            .thenReturn(bookings);
    Mockito.when(commentMapper.toModel(inputCommentDto)).thenReturn(commentWithoutItemAndAuthor);
    Mockito.when(commentMapper.toEntity(Mockito.any(Comment.class))).thenReturn(commentEntityInput);
    Mockito.when(commentRepository.save(commentEntityInput)).thenReturn(commentEntitySaved);
    Mockito.when(commentMapper.toModel(commentEntitySaved)).thenReturn(commentSaved);
    Mockito.when(commentMapper.toDto(commentSaved)).thenReturn(commentDto);

    OutputCommentDto resultComment = itemService.addComment(inputCommentDto, itemId, userId);
    assertEquals(commentDto, resultComment);
  }
}