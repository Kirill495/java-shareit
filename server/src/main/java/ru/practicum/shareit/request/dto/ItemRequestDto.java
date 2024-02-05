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
//  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "YYYY-MM-DD'T'HH:mm:ss")
  private LocalDateTime created;
  private Collection<ItemRequestDto.Item> items;

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
