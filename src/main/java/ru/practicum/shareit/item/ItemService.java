package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dao.ItemDao;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.validation.CreateNewItemInfo;
import ru.practicum.shareit.item.validation.UpdateItemInfo;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.dao.UserDao;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validation;
import javax.validation.Validator;
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

    validateItemInput(itemDto, CreateNewItemInfo.class);
    Item item = ItemMapper.toItem(itemDto, user);

    return ItemMapper.toItemDto(itemRepository.addNewItem(userId, item));
  }

  public ItemDto updateItem(int userId, int itemId, ItemDto itemDto) {
    userRepository.getUser(userId);
    validateItemInput(itemDto, UpdateItemInfo.class);
    Item item = itemRepository.getUserItem(userId, itemId);
    Item newItem = ItemMapper.toItem(itemDto);

    if (newItem.getAvailable() != null) {
      item.setAvailable(newItem.getAvailable());
    }
    if (newItem.getName() != null) {
      item.setName(newItem.getName());
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

  private <T> void validateItemInput(ItemDto itemDto, Class<T> className) {
    Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
    Set<ConstraintViolation<ItemDto>> violations = validator.validate(itemDto, className);
    if (!violations.isEmpty()) {
      StringBuilder sb = new StringBuilder();
      for (ConstraintViolation<ItemDto> violation : violations) {
        sb.append(violation.getPropertyPath());
        sb.append(": ");
        sb.append(violation.getMessage());
      }
      throw new ConstraintViolationException(sb.toString(), violations);
    }
  }
}
