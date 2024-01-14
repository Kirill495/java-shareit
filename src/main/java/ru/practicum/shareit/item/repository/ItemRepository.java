package ru.practicum.shareit.item.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.item.entity.ItemEntity;
import ru.practicum.shareit.user.entity.UserEntity;

import java.util.List;

public interface ItemRepository extends JpaRepository<ItemEntity, Integer> {

  ItemEntity findByOwnerAndId(UserEntity owner, int id);

  List<ItemEntity> findByOwnerOrderById(UserEntity owner);

  @Query("SELECT item " +
          "FROM ItemEntity as item " +
          "WHERE item.available = TRUE " +
          "AND (UPPER(item.name) like %?1% OR UPPER(item.description) like %?1%)")
  List<ItemEntity> searchAvailableItems(String searchTextName);
}
