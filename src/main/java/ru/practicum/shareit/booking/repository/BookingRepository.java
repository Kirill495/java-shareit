package ru.practicum.shareit.booking.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.shareit.booking.dto.ItemNearestBooking;
import ru.practicum.shareit.booking.entity.BookingEntity;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.entity.ItemEntity;
import ru.practicum.shareit.user.entity.UserEntity;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

public interface BookingRepository extends JpaRepository<BookingEntity, Integer> {

  @Query("SELECT b " +
          "FROM BookingEntity b " +
          "JOIN FETCH b.item AS i " +
          "WHERE b.id = ?2 " +
          "AND (b.booker = ?1 OR i.owner = ?1) " +
          "ORDER BY b.start DESC")
  BookingEntity getUserRelatedBookingById(UserEntity user, int bookingId);

  @Query("SELECT DISTINCT b " +
          "FROM BookingEntity AS b " +
          "JOIN FETCH b.item AS i " +
          "JOIN FETCH i.owner AS o " +
          "WHERE b.id = ?1")
  BookingEntity getBookingWithAllPropertiesById(int bookingId);

  @Query(value = "SELECT DISTINCT b " +
          "FROM BookingEntity AS b " +
          "JOIN FETCH b.booker AS u " +
          "WHERE b.item = ?1 AND b.start < CURRENT_TIMESTAMP " +
          "   AND b.status != 'REJECTED' " +
          "ORDER BY b.start DESC")
  BookingEntity getLastBooking(ItemEntity item, PageRequest page);

  @Query(value = "SELECT subquery.item_id AS itemId, bookings.id AS bookingId, bookings.booker_id AS bookerId " +
          "FROM " +
          "(SELECT items.id as item_id, MAX(bookings.end_date) as end_date " +
          "FROM items as items " +
          "   LEFT JOIN bookings as bookings " +
          "     ON items.id = bookings.item_id " +
          "     AND bookings.end_date < CURRENT_TIMESTAMP " +
          "WHERE items.id in (:item_ids) " +
          "GROUP BY items.id) AS subquery " +
          "JOIN bookings AS bookings " +
          "   ON subquery.item_id = bookings.item_id " +
          "     AND subquery.end_date = bookings.end_date",
        nativeQuery = true)
  List<ItemNearestBooking> getLastBookings(@Param("item_ids") Collection<Integer> itemIds);

  @Query(value = "SELECT subquery.item_id AS itemId, bookings.id AS bookingId, bookings.booker_id AS bookerId " +
          "FROM " +
          "(SELECT items.id as item_id, MIN(bookings.start_date) as start_date " +
          "FROM items as items " +
          "   LEFT JOIN bookings as bookings " +
          "     ON items.id = bookings.item_id " +
          "     AND bookings.start_date > CURRENT_TIMESTAMP " +
          "WHERE items.id in (?1) " +
          "GROUP BY items.id) AS subquery " +
          "JOIN bookings AS bookings " +
          "   ON subquery.item_id = bookings.item_id " +
          "     AND subquery.start_date = bookings.start_date",
          nativeQuery = true)
  List<ItemNearestBooking> getNextBookings(List<Integer> itemIds);

  @Query("SELECT DISTINCT b " +
          "FROM BookingEntity AS b " +
          "JOIN FETCH b.booker AS u " +
          "WHERE b.item = ?1 AND b.start > CURRENT_TIMESTAMP " +
          "   AND b.status != 'REJECTED' " +
          "ORDER BY b.start")
  BookingEntity getNextBooking(ItemEntity item, PageRequest page);

  Page<BookingEntity> findByItemOwner(UserEntity itemOwner, Pageable pageable);

  Page<BookingEntity> findByItemOwnerAndStartBeforeAndEndAfter(
          UserEntity itemOwner, LocalDateTime now1, LocalDateTime now2, Pageable pageable);

  Page<BookingEntity> findByItemOwnerAndEndBefore(UserEntity itemOwner, LocalDateTime dateTime, Pageable pageable);

  Page<BookingEntity> findByItemOwnerAndStartAfter(UserEntity user, LocalDateTime dateTime, Pageable pageable);

  Page<BookingEntity> findByItemOwnerAndStatus(UserEntity itemOwner, BookingStatus status, Pageable pageable);

  Page<BookingEntity> findByBooker(UserEntity booker, Pageable pageable);

  // Future
  Page<BookingEntity> findByBookerAndStartAfter(UserEntity booker, LocalDateTime dateTime, Pageable pageable);

  //Past
  Page<BookingEntity> findByBookerAndEndBefore(UserEntity booker, LocalDateTime dateTime, Pageable pageable);

  // Present
  Page<BookingEntity> findByBookerAndStartBeforeAndEndAfter(UserEntity booker, LocalDateTime currentTime1, LocalDateTime currentTime2, Pageable pageable);

  // WAITING, REJECTED
  Page<BookingEntity> findByBookerAndStatus(UserEntity booker, BookingStatus status, Pageable pageable);

  List<BookingEntity> findByBookerAndItemAndEndBefore(UserEntity booker, ItemEntity item, LocalDateTime currentTime);
}
