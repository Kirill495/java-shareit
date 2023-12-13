package ru.practicum.shareit.request;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import ru.practicum.shareit.user.User;

import java.time.LocalDate;

@Data
@RequiredArgsConstructor
public class ItemRequest {
   private Integer id;
   private String Description;
   private User requestor;
   private LocalDate created;
}
