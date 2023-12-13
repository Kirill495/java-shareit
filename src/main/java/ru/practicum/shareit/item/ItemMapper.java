package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

public class ItemMapper {
   public static ItemDto toItemDto(Item item) {
      return ItemDto.builder()
              .withId(item.getId())
              .withTitle(item.getTitle())
              .withDescription(item.getDescription())
              .withOwner(item.getOwner())
              .withAvailable(item.getAvailable()).build();
   }
}
