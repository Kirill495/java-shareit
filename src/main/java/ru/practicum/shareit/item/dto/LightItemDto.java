package ru.practicum.shareit.item.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.item.model.Item;

@Data
@Builder(setterPrefix = "with")
public class LightItemDto {
  private Integer id;
  private String name;

  public static LightItemDto toLightItem(Item item) {
    if (item == null) {
      return null;
    }
    return LightItemDto.builder()
            .withId(item.getId())
            .withName(item.getName())
            .build();
  }
}
