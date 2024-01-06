package ru.practicum.shareit.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.dto.LightItemDto;
import ru.practicum.shareit.user.dto.LightUserDto;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(setterPrefix = "with")
public class BookingDto {

  private Integer id;
  private LightUserDto booker;
  private LightItemDto item;
  private String start;
  private String end;
  private BookingStatus status;

}
