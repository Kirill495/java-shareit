package ru.practicum.shareit.request.model;

import lombok.Getter;
import lombok.Setter;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

@Getter
@Setter
public class ItemRequest {
   private Integer id;
   private String description;
   private LocalDateTime created;
   private User author;
}
