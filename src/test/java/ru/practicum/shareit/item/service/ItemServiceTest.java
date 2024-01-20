package ru.practicum.shareit.item.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.item.dto.item.ItemResponse;
import ru.practicum.shareit.item.entity.ItemEntity;
import ru.practicum.shareit.item.exceptions.ItemNotFoundException;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.entity.UserEntity;
import ru.practicum.shareit.user.exceptions.UserNotFoundException;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
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
  BookingMapper bookingMapper;
  @Mock
  CommentRepository commentRepository;

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

    when(userRepository.findById(userId)).thenReturn(Optional.of(userEntity));
    when(userMapper.toModel(userEntity)).thenReturn(user);
    when(itemRepository.findById(itemId)).thenReturn(Optional.of(itemEntity));
    when(itemMapper.toModel(itemEntity)).thenReturn(itemModel);
    when(itemMapper.toResponse(itemModel)).thenReturn(control);

    ItemResponse itemResponse = itemService.getItem(userId, itemId);

    assertEquals(control, itemResponse);

  }

  @Test
  void getItem_whenUserIsNotFound_thenThrowUserNotFoundException() {
    int userId = 0;
    int itemId = 1;
    when(userRepository.findById(userId)).thenReturn(Optional.empty());

    assertThrows(UserNotFoundException.class, () -> itemService.getItem(userId, itemId));

    verify(userRepository, times(1)).findById(userId);
  }

  @Test
  void getItem_whenItemIsNotFound_thenThrowItemNotFoundException() {
    int userId = 1;
    int itemId = 0;
    when(userRepository.findById(userId)).thenReturn(Optional.of(new UserEntity(userId, "Vlad", "vlad@emael.org")));
    when(itemRepository.findById(itemId)).thenThrow(ItemNotFoundException.class);
    assertThrows(ItemNotFoundException.class, () -> itemService.getItem(userId, itemId));

    verify(userRepository, times(1)).findById(userId);
    verify(itemRepository, times(1)).findById(itemId);
  }

  @Test
  void createNewItem_whenInputIsCorrect_thenReturnNewItem() {


  }
}