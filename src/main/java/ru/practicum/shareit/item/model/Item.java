package ru.practicum.shareit.item.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;

@Data
@Builder(setterPrefix = "with")
@Entity
@Table(name = "items")
@AllArgsConstructor
@NoArgsConstructor
public class Item {
   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Integer id;

   @ManyToOne(fetch = FetchType.LAZY, targetEntity = User.class)
   @JoinColumn(name = "owner_id")
   private User owner;

   @Column(nullable = false)
   private String name;

   @Column(length = 250)
   private String description;

   @Column(nullable = false)
   private Boolean available;

   @Transient
   private ItemRequest request;
}
