package ru.practicum.shareit.item.repository.memory;

import ru.practicum.shareit.item.entity.ItemEntity;

import java.util.List;

public interface ItemDao {

  ItemEntity getItem(int itemId);

  List<ItemEntity> getUserItems(int userId);

  ItemEntity getUserItem(int userId, int itemId);

  ItemEntity addNewItem(int userId, ItemEntity item);

  ItemEntity updateItem(int userId, ItemEntity item);

  List<ItemEntity> searchForItems(String text);

}
