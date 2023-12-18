package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

public class ItemMapper {
   public static ItemDto toItemDto(Item item) {
      return ItemDto.builder()
              .withId(item.getId())
              .withName(item.getName())
              .withDescription(item.getDescription())
              .withAvailable(item.getAvailable()).build();
   }

   public static Item toItem(ItemDto itemDto) {
      return Item.builder()
              .withId(itemDto.getId())
              .withName(itemDto.getName())
              .withDescription(itemDto.getDescription())
              .withAvailable(itemDto.getAvailable())
              .build();
   }

   public static Item toItem(ItemDto itemDto, User user) {
      Item item = toItem(itemDto);
      item.setOwner(user);
      return item;
   }
}
