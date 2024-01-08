package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.booking.dto.LightBookingDTO;
import ru.practicum.shareit.comment.Dto.OutputCommentDto;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.user.dto.LightUserDto;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(setterPrefix = "with")
public class ItemDto {
   private Integer id;
   private LightUserDto owner;
   private String name;
   private String description;
   private Boolean available;
   private LightBookingDTO lastBooking;
   private LightBookingDTO nextBooking;
   private ItemRequest request;
   private List<OutputCommentDto> comments;
}
