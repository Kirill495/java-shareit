package ru.practicum.shareit.booking;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.shareit.booking.dto.ItemNearestBooking;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Integer> {

  @Query("SELECT b " +
          "FROM Booking b " +
          "JOIN FETCH b.item AS i " +
          "WHERE b.id = ?2 " +
          "AND (b.booker = ?1 OR i.owner = ?1) " +
          "ORDER BY b.start DESC")
  Booking getUserRelatedBookingById(User user, int bookingId);

  @Query("SELECT b " +
          "FROM Booking b " +
          "JOIN FETCH b.item AS i " +
          "WHERE b.id = ?2 " +
          "AND (i.owner = ?1)")
  Booking getByItemOwnerAndId(User user, int bookingId);

  @Query("SELECT DISTINCT b " +
          "FROM Booking AS b " +
          "JOIN FETCH b.item AS i " +
          "JOIN FETCH i.owner AS o " +
          "WHERE b.id = ?1")
  Booking getBookingWithAllPropertiesById(int bookingId);

  @Query(value = "SELECT DISTINCT b " +
          "FROM Booking AS b " +
          "JOIN FETCH b.booker AS u " +
          "WHERE b.item = ?1 AND b.end < CURRENT_TIMESTAMP " +
          "ORDER BY b.start DESC")
  Booking getLastBooking(Item item, PageRequest page);

//  @Query(value =
//          "SELECT items.id as itemId, bookings.id as bookingId, bookings.booker_id as bookerId " +
//                  "FROM items AS items " +
//                  " JOIN bookings AS bookings " +
//                  " ON items.id = bookings.item_id " +
////            "   AND bookings.end_date < CURRENT_TIMESTAMP " +
//                  "WHERE items.id IN (:item_ids) order by item_id",
//          nativeQuery = true)
//
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
          "FROM Booking AS b " +
          "JOIN FETCH b.booker AS u " +
          "WHERE b.item = ?1 AND b.start > CURRENT_TIMESTAMP " +
          "ORDER BY b.start")
  Booking getNextBooking(Item item, PageRequest page);

  @Query("SELECT b " +
          "FROM Booking AS b " +
          "JOIN FETCH b.item AS i " +
          "WHERE i.owner = ?1 " +
          "ORDER BY b.start DESC")
  List<Booking> getAllByItemOwner(User user);

  @Query("SELECT b " +
          "FROM Booking AS b " +
          "JOIN FETCH b.item AS i " +
          "WHERE i.owner = ?1 " +
          "   AND CURRENT_TIMESTAMP BETWEEN b.start AND b.end " +
          "ORDER BY b.start DESC")
  List<Booking> getCurrentByItemOwner(User user);

  @Query("SELECT b " +
          "FROM Booking AS b " +
          "JOIN FETCH b.item AS i " +
          "WHERE i.owner = ?1 " +
          "   AND CURRENT_TIMESTAMP > b.end " +
          "ORDER BY b.start DESC")
  List<Booking> getPastByItemOwner(User user);

  @Query("SELECT b " +
          "FROM Booking AS b " +
          "JOIN FETCH b.item AS i " +
          "WHERE i.owner = ?1 " +
          "   AND CURRENT_TIMESTAMP < b.start " +
          "ORDER BY b.start DESC")
  List<Booking> getFutureByItemOwner(User user);

  @Query("SELECT b " +
          "FROM Booking AS b " +
          "JOIN FETCH b.item AS i " +
          "WHERE i.owner = ?1 " +
          "   AND b.status = ?2 " +
          "ORDER BY b.start DESC")
  List<Booking> getByItemOwnerAndStatus(User user, BookingStatus status);

  List<Booking> findByBookerOrderByStartDesc(User booker);

  // Future
  List<Booking> findByBookerAndStartAfterOrderByStartDesc(User booker, LocalDateTime dateTime);

  //Past
  List<Booking> findByBookerAndEndBeforeOrderByStartDesc(User booker, LocalDateTime dateTime);

  // Present
  List<Booking> findByBookerAndStartBeforeAndEndAfterOrderByEndDescId(User booker, LocalDateTime currentTime1, LocalDateTime currentTime2);

  // WAITING, REJECTED
  List<Booking> findByBookerAndStatusOrderByStartDesc(User booker, BookingStatus status);

  List<Booking> findByBookerAndItemAndEndBefore(User booker, Item item, LocalDateTime currentTime);
}
