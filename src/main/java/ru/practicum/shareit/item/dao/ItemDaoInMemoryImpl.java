package ru.practicum.shareit.item.dao;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.exceptions.UserItemNotFoundException;
import ru.practicum.shareit.item.model.Item;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
public class ItemDaoInMemoryImpl implements ItemDao {

  private static Map<Integer, Map<Integer, Item>> allItems = new HashMap<>();
  private static Integer currentItemId = 0;

  @Override
  public Item getItem(int itemId) {
    return allItems.values().stream()
            .map(itemsWithId -> itemsWithId.get(itemId))
            .findAny().orElse(null);
  }

  @Override
  public Item getUserItem(int userId, int itemId) {
    if (!allItems.containsKey(userId)) {
      throw new UserItemNotFoundException(String.format("Предметы пользователя %d не найдены", userId));
    }
    if (!allItems.get(userId).containsKey(itemId)) {
      throw new UserItemNotFoundException(String.format("Предмет %d пользователя %d не найден", itemId, userId));
    }
    return allItems.get(userId).get(itemId);
  }

  @Override
  public List<Item> getUserItems(int userId) {
    if (!allItems.containsKey(userId)) {
      return Collections.emptyList();
    }
    return new ArrayList<>(allItems.get(userId).values());
  }

  @Override
  public Item addNewItem(int userId, Item item) {
    item.setId(++currentItemId);
    if (!allItems.containsKey(userId)) {
      Map<Integer, Item> items = new HashMap<>();
      items.put(item.getId(), item);
      allItems.put(userId, items);
    } else {
      allItems.get(userId).put(item.getId(), item);
    }
    return item;
  }

  @Override
  public Item updateItem(int userId, Item item) {
    allItems.get(userId).put(item.getId(), item);
    return allItems.get(userId).get(item.getId());
  }

  @Override
  public List<Item> searchForItems(String text) {
    List<Item> result = new ArrayList<>();
    for (Map<Integer, Item> itemsSet : allItems.values()) {
      result.addAll(itemsSet.values().stream()
              .filter(item -> ((item.getTitle().toUpperCase().contains(text.toUpperCase()) && item.getAvailable())
                            || (item.getDescription().toUpperCase().contains(text.toUpperCase()) && item.getAvailable())))
              .collect(Collectors.toList()));
    }
    return result;
  }
}
