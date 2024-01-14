package ru.practicum.shareit.item.dto.item;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.item.validators.AtLeastOneNonNullField;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@AtLeastOneNonNullField
public class UpdateItemRequest {
   private String name;
   private String description;
   private Boolean available;
}
