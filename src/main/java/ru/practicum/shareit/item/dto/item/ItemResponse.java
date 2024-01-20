package ru.practicum.shareit.item.dto.item;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(setterPrefix = "with")
public class ItemResponse {
   private Integer id;
   private User owner;
   private String name;
   private String description;
   private Boolean available;
   private Booking lastBooking;
   private Booking nextBooking;
   private Integer requestId;
   private List<Comment> comments;

   @Data
   public static class Booking {
      private Integer id;
      private Integer bookerId;
   }

   @Data
   public static class User {
      private Integer id;
   }

   @Data
   @AllArgsConstructor
   public static class Comment {
      private Integer id;
      private String text;
      private String authorName;
      private String created;
   }
}
