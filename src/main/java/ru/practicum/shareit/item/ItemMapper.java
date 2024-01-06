package ru.practicum.shareit.item;

import lombok.NoArgsConstructor;
import ru.practicum.shareit.item.dto.InputNewItemDTO;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.LightItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserMapper;

@NoArgsConstructor
public class ItemMapper {
   public static ItemDto toItemDto(Item item) {
      return ItemDto.builder()
              .withId(item.getId())
              .withName(item.getName())
              .withOwner(UserMapper.toLightUserDto(item.getOwner()))
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

   public static Item toItem(InputNewItemDTO itemDTO) {
      return Item.builder()
              .withName(itemDTO.getName())
              .withDescription(itemDTO.getDescription())
              .withAvailable(itemDTO.getAvailable())
              .build();
   }

   public static LightItemDto lightItemDto(Item item) {
      return LightItemDto.builder()
              .withId(item.getId())
              .withName(item.getName())
              .build();
   }
}
