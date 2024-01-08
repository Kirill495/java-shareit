package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.comment.Dto.InputCommentDto;
import ru.practicum.shareit.comment.Dto.OutputCommentDto;
import ru.practicum.shareit.item.dto.InputNewItemDTO;
import ru.practicum.shareit.item.dto.InputUpdatedItemDTO;
import ru.practicum.shareit.item.dto.ItemDto;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {

  private final ItemService service;

  @PostMapping
  public ItemDto createNewItem(@RequestHeader(value = "X-Sharer-User-Id") int userId,
                               @RequestBody @Valid InputNewItemDTO itemDto) {
    return service.createNewItem(userId, itemDto);
  }

  @PatchMapping("/{itemId}")
  public ItemDto updateItem(@RequestHeader(value = "X-Sharer-User-Id") int userId,
                            @PathVariable("itemId") int itemId,
                            @RequestBody @Valid InputUpdatedItemDTO itemDto) {

    return service.updateItem(userId, itemId, itemDto);
  }

  @GetMapping("/{itemId}")
  public ItemDto getItem(@RequestHeader("X-Sharer-User-Id") int userId,
                         @PathVariable("itemId") int itemId) {
    return service.getItem(userId, itemId);
  }

  @GetMapping
  public List<ItemDto> getItems(@RequestHeader(value = "X-Sharer-User-Id") Integer userId) {
    return service.getUserItems(userId);
  }

  @PostMapping("/{itemId}/comment")
  public OutputCommentDto addComment(@RequestHeader(value = "X-Sharer-User-Id") Integer userId,
                                     @PathVariable("itemId") Integer itemId,
                                     @RequestBody @Valid InputCommentDto input) {
    return service.addComment(input, itemId, userId);
  }

  @GetMapping("/search")
  public List<ItemDto> searchItems(@RequestParam(value = "text") String text) {
    return service.searchItems(text);
  }
}
