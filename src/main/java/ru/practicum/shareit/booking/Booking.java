package ru.practicum.shareit.booking;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

import java.time.LocalDate;

@Data
@RequiredArgsConstructor
public class Booking {
   private Integer id;
   private User booker;
   private Item item;
   private LocalDate start;
   private LocalDate end;
   private BookingStatus status;
   private String review;
}
