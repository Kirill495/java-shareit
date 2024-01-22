package ru.practicum.shareit.request.model;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@EqualsAndHashCode(of = {"id", "description"})
public class ItemRequest {
   private Integer id;
   private String description;
   private LocalDateTime created;
   private User author;
}
