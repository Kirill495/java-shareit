package ru.practicum.shareit.item;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Integer> {

  Item findByOwnerAndId(User owner, int id);

  List<Item> findByOwnerOrderById(User owner);

  @Query("SELECT item " +
          "FROM Item as item " +
          "WHERE item.available = TRUE " +
          "AND (UPPER(item.name) like %?1% OR UPPER(item.description) like %?1%)")
  List<Item> searchAvailableItems(String searchTextName);
}
