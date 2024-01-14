package ru.practicum.shareit.item.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.entity.UserEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

@Data
@Builder(setterPrefix = "with")
@Entity
@Table(name = "items")
@AllArgsConstructor
@NoArgsConstructor
public class ItemEntity {
   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Integer id;

   @ManyToOne(fetch = FetchType.LAZY, targetEntity = UserEntity.class)
   @JoinColumn(name = "owner_id")
   private UserEntity owner;

   @Column(nullable = false)
   private String name;

   @Column(length = 250)
   private String description;

   @Column(nullable = false)
   private Boolean available;

   @Transient
   private ItemRequest request;
}
