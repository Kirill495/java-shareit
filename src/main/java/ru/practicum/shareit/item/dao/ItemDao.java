package ru.practicum.shareit.item.dao;

import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemDao {

  Item getItem(int itemId);

  List<Item> getUserItems(int userId);

  Item getUserItem(int userId, int itemId);

  Item addNewItem(int userId, Item item);

  Item updateItem(int userId, Item item);

  List<Item> searchForItems(String text);

}
