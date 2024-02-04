package ru.practicum.shareit.item.dto.item;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
public class NewItemRequest {
   private String name;
   private String description;
   private Boolean available;
   private Integer requestId;

}
