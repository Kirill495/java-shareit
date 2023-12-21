package ru.practicum.shareit.item;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.item.dao.ItemDaoInMemoryImpl;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.dao.UserDao;
import ru.practicum.shareit.user.dao.UserDaoInMemoryImpl;
import ru.practicum.shareit.user.exceptions.UserNotFoundException;
import ru.practicum.shareit.user.model.User;

import javax.validation.ConstraintViolationException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
class ItemServiceTest {

  @Mock
  private UserDao userRepository;

  @Test
  @DirtiesContext
  void createNewItem() {
    User user = User.builder().withId(1).withName("Ivan").build();
    ItemService itemService = new ItemService(userRepository, new ItemDaoInMemoryImpl());
    ItemDto itemDto = ItemDto.builder().withName("Fork").withDescription("Wooden fork").withAvailable(true).build();
    when(userRepository.getUser(1)).thenReturn(user);
    ItemDto returnedItemDto = itemService.createNewItem(1, itemDto);
    assertEquals(1, returnedItemDto.getId());
  }

  @Test
  @DirtiesContext
  void createNewItemShouldFail() {
    User user = User.builder().withId(1).withName("Ivan").build();
    when(userRepository.getUser(1)).thenReturn(user);

    ItemService itemService = new ItemService(userRepository, new ItemDaoInMemoryImpl());
    ItemDto itemDto = ItemDto.builder().withName("Fork").withDescription("Wooden fork").build();
    assertThrows(ConstraintViolationException.class, () -> itemService.createNewItem(1, itemDto));
  }

  @Test
  @DirtiesContext
  void getUnknownItem() {
    User user = User.builder().withId(1).withName("Ivan").build();
    when(userRepository.getUser(1)).thenReturn(user);
    ItemService itemService = new ItemService(userRepository, new ItemDaoInMemoryImpl());
    assertNull(itemService.getItem(1, 2));
  }

  @Test
  void searchForNonexistentItem() {
    ItemService itemService = new ItemService(userRepository, new ItemDaoInMemoryImpl());
    assertEquals(List.of(), itemService.searchItems("aaa"));
  }

  @Test
  void searchWithBlankString() {
    ItemService itemService = new ItemService(userRepository, new ItemDaoInMemoryImpl());
    assertEquals(List.of(), itemService.searchItems(""));
  }

  @Test
  @DirtiesContext
  void searchForExistentItem() {
    User user = User.builder().withId(1).withName("Ivan").build();
    when(userRepository.getUser(1)).thenReturn(user);
    ItemService itemService = new ItemService(userRepository, new ItemDaoInMemoryImpl());
    ItemDto itemDto = ItemDto.builder().withName("Fork").withDescription("Wooden fork").withAvailable(true).build();
    itemService.createNewItem(1, itemDto);
    itemDto.setId(1);
    assertEquals(List.of(itemDto), itemService.searchItems("wood"));
  }

  @Test
  void getUserItems() {
    int userId = 1;
    User user = User.builder().withId(userId).withName("Ivan").build();
    when(userRepository.getUser(userId)).thenReturn(user);
    ItemService itemService = new ItemService(userRepository, new ItemDaoInMemoryImpl());
    ItemDto itemDto = ItemDto.builder().withName("Fork").withDescription("Wooden fork").withAvailable(true).build();
    itemService.createNewItem(userId, itemDto);
    List<ItemDto> items = itemService.getUserItems(userId);
    assertEquals(1, items.size());
    itemDto.setId(1);
    assertEquals(itemDto, items.get(0));
  }

  @Test
  @DirtiesContext
  void getItemOfUnknownUser() {
    userRepository = new UserDaoInMemoryImpl();
    ItemService itemService = new ItemService(userRepository, new ItemDaoInMemoryImpl());
    assertThrows(UserNotFoundException.class, () -> itemService.getItem(1, 1));
  }

  @Test
  @DirtiesContext
  void testItemUpdate() {
    ItemService itemService = new ItemService(userRepository, new ItemDaoInMemoryImpl());
    int userId = 1;
    User user = User.builder().withId(userId).withName("Ivan").build();
    when(userRepository.getUser(userId)).thenReturn(user);
    ItemDto itemDto = ItemDto.builder().withName("Fork").withDescription("Wooden fork").withAvailable(true).build();
    itemService.createNewItem(userId, itemDto);
    String newItemName = "_Fork_";
    boolean newStatus = false;
    String newDescription = "_Wooden_fork_";
    ItemDto resItem = itemService.updateItem(userId, 1, ItemDto.builder().withName(newItemName).withAvailable(newStatus).withDescription(newDescription).build());

    assertEquals(newItemName, resItem.getName());
    assertEquals(newItemName, itemService.getItem(1, 1).getName());
    assertEquals(newStatus, itemService.getItem(1, 1).getAvailable());
    assertEquals(newDescription, itemService.getItem(1, 1).getDescription());
  }

  @Test
  @DirtiesContext
  void testItemUpdateWithBlankNameDescription() {
    ItemService itemService = new ItemService(userRepository, new ItemDaoInMemoryImpl());
    int userId = 1;
    User user = User.builder().withId(userId).withName("Ivan").build();
    when(userRepository.getUser(userId)).thenReturn(user);
    ItemDto itemDto = ItemDto.builder().withName("Fork").withDescription("Wooden fork").withAvailable(true).build();
    itemService.createNewItem(userId, itemDto);
    boolean newStatus = false;
    ItemDto resItem = itemService.updateItem(userId, 1, ItemDto.builder().withAvailable(newStatus).build());

    assertEquals(newStatus, itemService.getItem(1, 1).getAvailable());
  }
}
