package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.item.dto.comment.InputCommentDto;
import ru.practicum.shareit.item.dto.item.NewItemRequest;
import ru.practicum.shareit.item.dto.item.UpdateItemRequest;

import javax.validation.Valid;
import javax.validation.constraints.Positive;

@RestController
@RequestMapping("/items")
@Validated
@RequiredArgsConstructor
public class ItemController {

  private final ItemClient itemClient;

  @PostMapping
  public ResponseEntity<Object> createNewItem(@RequestHeader(value = "X-Sharer-User-Id") int userId,
                                      @RequestBody @Valid NewItemRequest itemDto) {
    return itemClient.createNewItem(userId, itemDto);
  }

  @PatchMapping("/{itemId}")
  public ResponseEntity<Object> updateItem(@RequestHeader(value = "X-Sharer-User-Id") int userId,
                                 @PathVariable("itemId") @Positive int itemId,
                                 @RequestBody @Valid UpdateItemRequest itemDto) {
    return itemClient.updateItem(itemId, userId, itemDto);
  }

  @GetMapping("/{itemId}")
  public ResponseEntity<Object> getItem(@RequestHeader("X-Sharer-User-Id") int userId,
                              @PathVariable("itemId") @Positive int itemId) {
    return itemClient.getItem(itemId, userId);
  }

  @GetMapping
  public ResponseEntity<Object> getItems(@RequestHeader(value = "X-Sharer-User-Id") Integer userId) {
    return itemClient.getItems(userId);
  }

  @PostMapping("/{itemId}/comment")
  public ResponseEntity<Object> addComment(@RequestHeader(value = "X-Sharer-User-Id") Integer userId,
                                     @PathVariable("itemId") @Positive int itemId,
                                     @RequestBody @Valid InputCommentDto input) {
    return itemClient.addComment(itemId, userId, input);

  }

  @GetMapping("/search")
  public ResponseEntity<Object> searchItems(@RequestParam(value = "text") String text) {
    return itemClient.searchItems(text);
  }
}
