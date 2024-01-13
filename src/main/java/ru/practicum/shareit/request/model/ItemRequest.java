package ru.practicum.shareit.request.model;

import lombok.Data;

import java.time.LocalDate;

@Data
public class ItemRequest {
   private Integer id;
   private String description;
   private LocalDate created;
}
