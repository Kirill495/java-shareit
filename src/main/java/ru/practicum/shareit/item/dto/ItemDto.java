package ru.practicum.shareit.item.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.user.User;

@Data
@Builder(setterPrefix = "with")
public class ItemDto {
   private Integer id;
   private User owner;
   private String title;
   private String description;
   private Boolean available;
   private ItemRequest request;
}
