package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dao.ItemDao;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.validation.CreateNewItemInfo;
import ru.practicum.shareit.item.validation.UpdateItemInfo;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.dao.UserDao;

import javax.validation.*;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemService {

  private final UserDao userRepository;
  private final ItemDao itemRepository;

  public ItemDto createNewItem(int userId, ItemDto itemDto) {

    User user = userRepository.getUser(userId);

    Item item = ItemMapper.toItem(itemDto, user);
    validateItemInput(item, CreateNewItemInfo.class);
    return ItemMapper.toItemDto(itemRepository.addNewItem(userId, item));
  }

  public ItemDto updateItem(int userId, int itemId, ItemDto itemDto) {
    userRepository.getUser(userId);
    Item item = itemRepository.getUserItem(userId, itemId);

    Item newItem = ItemMapper.toItem(itemDto);
    validateItemInput(newItem, UpdateItemInfo.class);

    if (newItem.getAvailable() != null) {
      item.setAvailable(newItem.getAvailable());
    }
    if (newItem.getTitle() != null) {
      item.setTitle(newItem.getTitle());
    }
    if (newItem.getDescription() != null) {
      item.setDescription(newItem.getDescription());
    }
    return ItemMapper.toItemDto(itemRepository.updateItem(userId, item));
  }

  public ItemDto getItem(int userId, int itemId) {
    userRepository.getUser(userId);
    return ItemMapper.toItemDto(itemRepository.getItem(itemId));
  }

  public List<ItemDto> getUserItems(int userId) {
    userRepository.getUser(userId);
    return itemRepository
            .getUserItems(userId).stream()
            .map(ItemMapper::toItemDto)
            .collect(Collectors.toList());
  }

  public List<ItemDto> searchItems(String text) {
    if (text.isBlank()) {
      return Collections.emptyList();
    }
    return itemRepository
            .searchForItems(text).stream()
            .map(ItemMapper::toItemDto)
            .collect(Collectors.toList());
  }

  private <T> void validateItemInput(Item item, Class<T> className) {
    Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
    Set<ConstraintViolation<Item>> violations = validator.validate(item, className);
    if (!violations.isEmpty()) {
      StringBuilder sb = new StringBuilder();
      for (ConstraintViolation<Item> violation : violations) {
        sb.append(violation.getPropertyPath());
        sb.append(": ");
        sb.append(violation.getMessage());
      }
      throw new ConstraintViolationException(sb.toString(), violations);
    }
  }
}
