package ru.practicum.shareit.item.repository.memory;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.exceptions.UserItemNotFoundException;
import ru.practicum.shareit.item.entity.ItemEntity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
public class ItemDaoInMemoryImpl implements ItemDao {

  private static Map<Integer, Map<Integer, ItemEntity>> allItems = new HashMap<>();
  private static Integer currentItemId = 0;

  @Override
  public ItemEntity getItem(int itemId) {
    return allItems.values().stream()
            .map(itemsWithId -> itemsWithId.get(itemId))
            .findAny().orElse(null);
  }

  @Override
  public ItemEntity getUserItem(int userId, int itemId) {
    if (!allItems.containsKey(userId)) {
      throw new UserItemNotFoundException(String.format("Предметы пользователя %d не найдены", userId));
    }
    if (!allItems.get(userId).containsKey(itemId)) {
      throw new UserItemNotFoundException(String.format("Предмет %d пользователя %d не найден", itemId, userId));
    }
    return allItems.get(userId).get(itemId);
  }

  @Override
  public List<ItemEntity> getUserItems(int userId) {
    if (!allItems.containsKey(userId)) {
      return Collections.emptyList();
    }
    return new ArrayList<>(allItems.get(userId).values());
  }

  @Override
  public ItemEntity addNewItem(int userId, ItemEntity item) {
    item.setId(++currentItemId);
    if (!allItems.containsKey(userId)) {
      Map<Integer, ItemEntity> items = new HashMap<>();
      items.put(item.getId(), item);
      allItems.put(userId, items);
    } else {
      allItems.get(userId).put(item.getId(), item);
    }
    return item;
  }

  @Override
  public ItemEntity updateItem(int userId, ItemEntity item) {
    allItems.get(userId).put(item.getId(), item);
    return allItems.get(userId).get(item.getId());
  }

  @Override
  public List<ItemEntity> searchForItems(String text) {
    List<ItemEntity> result = allItems.values().stream()
            .flatMap(itemWithId -> itemWithId.values().stream())
            .filter(item -> (checkItem(item, text)))
            .collect(Collectors.toList());
    return result;
  }

  private static boolean checkItem(ItemEntity item, String text) {
    String name = item.getName().toUpperCase();
    String description = item.getDescription().toUpperCase();
    String preparedText = text.toUpperCase();

    return (item.getAvailable() && (name.contains(preparedText) || description.contains(preparedText)));
  }
}
