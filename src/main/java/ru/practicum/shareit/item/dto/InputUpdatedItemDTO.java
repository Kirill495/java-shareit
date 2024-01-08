package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.item.validators.AtLeastOneNonNullField;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(setterPrefix = "with")
@AtLeastOneNonNullField
public class InputUpdatedItemDTO {
//   private User owner;
   private String name;
   private String description;
   private Boolean available;
}
