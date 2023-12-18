package ru.practicum.shareit.user.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder(setterPrefix = "with")
public class User {
   private Integer id;
   private String name;
   private String email;
}
