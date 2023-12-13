package ru.practicum.shareit.user;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@Builder(setterPrefix = "with")
@RequiredArgsConstructor
public class User {
   private int id;
   private String name;
   private String email;
}
