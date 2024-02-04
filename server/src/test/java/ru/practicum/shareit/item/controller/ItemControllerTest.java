package ru.practicum.shareit.item.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.practicum.shareit.item.dto.comment.InputCommentDto;
import ru.practicum.shareit.item.dto.comment.OutputCommentDto;
import ru.practicum.shareit.item.dto.item.ItemResponse;
import ru.practicum.shareit.item.dto.item.NewItemRequest;
import ru.practicum.shareit.item.dto.item.UpdateItemRequest;
import ru.practicum.shareit.item.service.ItemServiceImpl;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ItemController.class)
class ItemControllerTest {

  @Autowired
  MockMvc mockMvc;
  @Autowired
  private ObjectMapper objectMapper;

  @MockBean
  ItemServiceImpl itemService;

  @SneakyThrows
  @Test
  void createNewItem() {
    NewItemRequest request = new NewItemRequest("fork", "silver fork", true, null);
    ItemResponse response = new ItemResponse();
    response.setId(1);
    response.setName("fork");
    response.setDescription("silver fork");
    response.setAvailable(true);
    when(itemService.createNewItem(anyInt(), any())).thenReturn(response);
    String result = mockMvc.perform(MockMvcRequestBuilders.post("/items")
                    .header("X-Sharer-User-Id", 1)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andReturn().getResponse().getContentAsString();
    ItemResponse itemResponse =  objectMapper.readValue(result, ItemResponse.class);
    assertEquals(response, itemResponse);
  }

  @SneakyThrows
  @Test
  void updateItem() {
    int userId = 1;
    int itemId = 1;
    UpdateItemRequest request = new UpdateItemRequest("_fork_", null, false);
    ItemResponse response = new ItemResponse();
    response.setId(itemId);
    response.setName("_fork_");
    response.setAvailable(false);
    when(itemService.updateItem(userId, itemId, request)).thenReturn(response);

    String result = mockMvc.perform(MockMvcRequestBuilders.patch("/items/{id}", itemId)
                    .header("X-Sharer-User-Id", 1)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andReturn().getResponse().getContentAsString();
    ItemResponse itemResponse = objectMapper.readValue(result, ItemResponse.class);
    assertEquals(response, itemResponse);

  }

  @SneakyThrows
  @Test
  void getItem_whenNoUserId_thenReturnBadRequest() {
    int itemId = 1;
    mockMvc.perform(MockMvcRequestBuilders.get("/items/{id}", itemId))
            .andExpect(status().isBadRequest());
    verify(itemService, never()).getItem(anyInt(), anyInt());
  }

  @SneakyThrows
  @Test
  void getItem_whenItemExists_thenReturnItem() {
    int itemId = 1;
    int userId = 1;
    ItemResponse response = new ItemResponse();
    response.setId(itemId);
    when(itemService.getItem(userId, itemId)).thenReturn(response);
    mockMvc.perform(MockMvcRequestBuilders.get("/items/{id}", itemId)
                    .header("X-Sharer-User-Id", userId))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(itemId));
    verify(itemService, times(1)).getItem(anyInt(), anyInt());
  }

  @SneakyThrows
  @Test
  void getItems() {
    int userId = 1;
    ItemResponse response1 = new ItemResponse();
    response1.setId(1);
    ItemResponse response2 = new ItemResponse();
    response2.setId(2);
    List<ItemResponse> responseList = List.of(response1, response2);

    when(itemService.getUserItems(userId)).thenReturn(responseList);

    mockMvc.perform(MockMvcRequestBuilders.get("/items")
                    .header("X-Sharer-User-Id", userId))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.[0].id").value(1))
            .andExpect(jsonPath("$.[1].id").value(2));

    verify(itemService, times(1)).getUserItems(anyInt());
  }

  @SneakyThrows
  @Test
  void addComment() {
    int itemId = 1;
    int userId = 1;
    String text = "ttttttt";
    InputCommentDto inputComment = new InputCommentDto();
    inputComment.setText(text);
    OutputCommentDto outputComment = new OutputCommentDto(1, text, "", "");

    when(itemService.addComment(inputComment, itemId, userId)).thenReturn(outputComment);

    String result = mockMvc.perform(MockMvcRequestBuilders.post("/items/{itemId}/comment", itemId)
                    .header("X-Sharer-User-Id", userId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(inputComment)))
            .andExpect(status().isOk())
            .andReturn().getResponse().getContentAsString();
    OutputCommentDto outputCommentFact = objectMapper.readValue(result, OutputCommentDto.class);

    assertEquals(outputComment, outputCommentFact);
    verify(itemService, times(1)).addComment(any(), eq(itemId), eq(userId));
  }

  @SneakyThrows
  @Test
  void searchItems() {

    String text = "ttttt";
    ItemResponse response1 = new ItemResponse();
    response1.setId(1);
    ItemResponse response2 = new ItemResponse();
    response2.setId(2);
    List<ItemResponse> responseList = List.of(response1, response2);
    when(itemService.searchItems(text)).thenReturn(responseList);

    mockMvc.perform(MockMvcRequestBuilders.get("/items/search")
              .header("X-Sharer-User-Id", 1)
              .param("text", text))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.[0].id").value(1))
            .andExpect(jsonPath("$.[0].id").value(1));
  }
}