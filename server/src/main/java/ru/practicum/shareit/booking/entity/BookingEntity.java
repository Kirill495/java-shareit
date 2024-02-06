package ru.practicum.shareit.booking.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.entity.ItemEntity;
import ru.practicum.shareit.user.entity.UserEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.time.LocalDateTime;


@Data
@Builder(setterPrefix = "with")
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "bookings")
public class BookingEntity {
   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Integer id;

   @ManyToOne(fetch = FetchType.LAZY, targetEntity = UserEntity.class)
   @JoinColumn(name = "booker_id")
   private UserEntity booker;

   @ManyToOne(fetch = FetchType.LAZY, targetEntity = ItemEntity.class)
   @JoinColumn(name = "item_id")
   private ItemEntity item;

   @Column(name = "start_date")
   private LocalDateTime start;

   @Column(name = "end_date")
   private LocalDateTime end;

   @Column(name = "status")
   @Enumerated(EnumType.STRING)
   private BookingStatus status;

   @Transient
   private String review;
}
