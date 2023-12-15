package ru.practicum.shareit.user.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder(setterPrefix = "with")
public class UserDto {
   private Integer id;
   private String name;
   private String email;
}
