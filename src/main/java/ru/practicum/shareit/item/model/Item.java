package ru.practicum.shareit.item.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

@Data
@AllArgsConstructor
@Builder(setterPrefix = "with")
public class Item {
  private Integer id;
  private User owner;
  private String name;
  private String description;
  private Boolean available;
  private ItemRequest request;
}
