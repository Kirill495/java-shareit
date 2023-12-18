package ru.practicum.shareit.item.model;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.user.model.User;

@Data
@Builder(setterPrefix = "with")
public class Item {
   private Integer id;
   private User owner;
   private String name;
   private String description;
   private Boolean available;
   private ItemRequest request;
}
