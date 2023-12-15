package ru.practicum.shareit.item.model;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.item.validation.CreateNewItemInfo;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.user.User;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Builder(setterPrefix = "with")
public class Item {
   private Integer id;
   private User owner;
   @NotNull(groups = CreateNewItemInfo.class)
   @NotBlank(groups = CreateNewItemInfo.class)
   private String title;
   @NotNull(groups = CreateNewItemInfo.class)
   @NotBlank(groups = CreateNewItemInfo.class)
   private String description;
   @NotNull(groups = CreateNewItemInfo.class)
   private Boolean available;
   private ItemRequest request;
}
