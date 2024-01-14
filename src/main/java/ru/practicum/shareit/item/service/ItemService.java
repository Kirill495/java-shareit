package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.comment.InputCommentDto;
import ru.practicum.shareit.item.dto.comment.OutputCommentDto;
import ru.practicum.shareit.item.dto.item.ItemResponse;
import ru.practicum.shareit.item.dto.item.NewItemRequest;
import ru.practicum.shareit.item.dto.item.UpdateItemRequest;

import java.util.List;

public interface ItemService {

  ItemResponse createNewItem(int userId, NewItemRequest request);

  ItemResponse updateItem(int userId, int itemId, UpdateItemRequest request);

  ItemResponse getItem(int userId, int itemId);

  List<ItemResponse> getUserItems(int userId);

  OutputCommentDto addComment(InputCommentDto inputCommentDto, Integer itemId, Integer userId);

  List<ItemResponse> searchItems(String text);
}
