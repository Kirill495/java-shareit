package ru.practicum.shareit.item.dto.item;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
public class NewItemRequest {
   @NotNull
   @NotBlank
   private String name;
   @NotNull
   @NotBlank
   private String description;
   @NotNull
   private Boolean available;

   private Integer requestId;
}
