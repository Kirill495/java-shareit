package ru.practicum.shareit.request.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Collection;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ItemRequestDto {
  private Integer id;
  private String description;
  private LocalDateTime created;
  private Collection<Item> items;

  @Setter
  @Getter
  @AllArgsConstructor
  public static class Item {
    private Integer id;
    private String name;
    private String description;
    private Boolean available;
    private Integer requestId;
  }
}
