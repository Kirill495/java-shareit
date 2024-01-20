package ru.practicum.shareit.user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserServiceImpl;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

//@ExtendWith(MockitoExtension.class)
@WebMvcTest(controllers = UserController.class)
class UserControllerIntegrationTest {

//  @InjectMocks
//  private UserController userController;

  @Autowired
  private ObjectMapper objectMapper;

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private UserServiceImpl userService;

  @MockBean
  private UserMapper userMapper;

  @SneakyThrows
  @Test
  void getUser() {
    int userId = 0;

    when(userService.getUser(userId)).thenReturn(new User(userId, "", ""));
    mockMvc.perform(get("/users/{id}", userId))
                    .andExpect(status().isOk());
    verify(userService).getUser(userId);
  }

//  @Test
//  void createUser() {
//  }
//
//  @Test
//  void updateUser() {
//  }
}