package ru.practicum.shareit.item;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.dao.UserDao;
import ru.practicum.shareit.user.model.User;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(SpringExtension.class)
@WebAppConfiguration
@AutoConfigureMockMvc
@SpringBootTest
class ItemControllerTest {

  @Autowired
  private ItemController itemController;
  @Autowired
  private ObjectMapper objectMapper;
  @Autowired
  private UserDao userDao;
  @Autowired
  private MockMvc mockMvc;

  @BeforeEach
  void setUp() {
//    mockMvc = MockMvcBuilders.standaloneSetup(itemController).build();
  }

  @Test
  void createNewItem() throws Exception {
    ItemDto forkDto = ItemDto.builder().withName("fork").withDescription("Silver description").withAvailable(true).build();

    String body = objectMapper.writeValueAsString(forkDto);
    userDao.addNewUser(User.builder().withId(1).withName("ivan").build());
//    when(userDao.getUser(1)).thenReturn();
    mockMvc
            .perform(
                    MockMvcRequestBuilders.post("/items")
                            .header("X-Sharer-User-Id", 1)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(body))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.name").value("fork"))
            .andExpect(jsonPath("$.description").value("Silver description"));
  }
}