package ru.practicum.shareit.item.model;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.user.User;

@Data
@RequiredArgsConstructor
public class Item {
   private Integer id;
   private User owner;
   private String title;
   private String description;
   private Boolean available;
   private ItemRequest request;
}
