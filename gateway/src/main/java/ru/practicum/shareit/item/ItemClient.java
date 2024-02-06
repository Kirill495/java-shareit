package ru.practicum.shareit.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.client.BaseClient;
import ru.practicum.shareit.item.dto.comment.InputCommentDto;
import ru.practicum.shareit.item.dto.item.NewItemRequest;
import ru.practicum.shareit.item.dto.item.UpdateItemRequest;

import java.util.Map;

@Service
public class ItemClient extends BaseClient {

  private static final String API_PREFIX = "/items";

  @Autowired
  public ItemClient(@Value("${server.url}") String serverUrl, RestTemplateBuilder builder) {
    super(
            builder
                    .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                    .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                    .build()
    );
  }

  public ResponseEntity<Object> createNewItem(int userId, NewItemRequest itemDto) {
    return post("", userId, itemDto);
  }

  public ResponseEntity<Object> updateItem(int itemId, int userId, UpdateItemRequest itemDto) {
    return patch("/{itemId}", userId, Map.of("itemId", itemId), itemDto);
  }

  public ResponseEntity<Object> getItem(int itemId, int userId) {
    return get("/" + itemId, userId);
  }

  public ResponseEntity<Object> getItems(int userId) {
    return get("", userId);
  }

  public ResponseEntity<Object> addComment(int itemId, int userId, InputCommentDto input) {
    return post("/{itemId}/comment", userId, Map.of("itemId", itemId), input);
  }

  public ResponseEntity<Object> searchItems(String text) {
    return get("/search?text={text}", null, Map.of("text", text));
  }
}
