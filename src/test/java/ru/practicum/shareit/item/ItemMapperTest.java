package ru.practicum.shareit.item;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import static org.junit.jupiter.api.Assertions.*;

class ItemMapperTest {

  @Test
  void testToItemDto() {
    User user =  User.builder().withId(1).withName("Ivan").withEmail("ivan@email.io").build();
    Item item = Item.builder().withId(1).withOwner(user).withName("fork").withDescription("silver fork").withAvailable(true).build();
    ItemDto itemDto = ItemMapper.toItemDto(item);
    assertEquals(1, itemDto.getId());
    assertEquals("fork", itemDto.getName());
    assertEquals("silver fork", itemDto.getDescription());
    assertEquals(true, itemDto.getAvailable());
  }

  @Test
  void testToItemWithItemDto() {
    User user =  User.builder().withId(1).withName("Ivan").withEmail("ivan@email.io").build();
    ItemDto itemDto = ItemDto.builder().withId(1).withAvailable(false).withName("Spoon").withDescription("steel spoon").withOwner(user).build();
    Item item = ItemMapper.toItem(itemDto);
    Assertions.assertEquals(1, item.getId());
    assertEquals("Spoon", item.getName());
    assertEquals("steel spoon", item.getDescription());
    assertEquals(false, item.getAvailable());
    assertNull(item.getOwner());
  }

  @Test
  void testToItemWithItemDtoAndOwner() {
    User user =  User.builder().withId(1).withName("Ivan").withEmail("ivan@email.io").build();
    ItemDto itemDto = ItemDto.builder().withId(1).withAvailable(false).withName("Spoon").withDescription("steel spoon").build();
    Item item = ItemMapper.toItem(itemDto, user);
    Assertions.assertEquals(1, item.getId());
    assertEquals("Spoon", item.getName());
    assertEquals("steel spoon", item.getDescription());
    assertEquals(false, item.getAvailable());
    assertEquals(user, item.getOwner());
  }
}