package ru.practicum.shareit.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.user.dto.NewUserRequest;
import ru.practicum.shareit.user.dto.UpdateUserRequest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = UserController.class)
@ActiveProfiles(profiles = {"test"})
class UserControllerIntegrationTest {

  @Autowired
  private ObjectMapper objectMapper;
  @Autowired
  private MockMvc mockMvc;
  @MockBean
  private UserClient userClient;

  @SneakyThrows
  @Test
  @DisplayName("Test should return BAD_REQUEST for create user request when user name is blank")
  void createUser_whenUserNameIsEmpty_ThenReturnBadRequest() {
    String email = "vladimir@email.org";

    NewUserRequest inputUser = new NewUserRequest("", email);
    mockMvc.perform(
                    post("/users")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(inputUser)))
            .andExpect(status().isBadRequest());
  }

  @SneakyThrows
  @Test
  @DisplayName("Test should return BAD_REQUEST for crate user request when user name is null")
  void createUser_whenUserNameIsNull_ThenReturnBadRequest() {
    String email = "vladimir@email.org";

    NewUserRequest inputUser = new NewUserRequest(null, email);
    mockMvc.perform(
                    post("/users")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(inputUser)))
            .andExpect(status().isBadRequest());
  }

  @SneakyThrows
  @Test
  @DisplayName("Test should return BAD_REQUEST for request create user when user email is blank")
  void createUser_whenUserEmailIsEmpty_ThenReturnBadRequest() {
    String name = "Vladimir";

    NewUserRequest inputUser = new NewUserRequest(name, "");
    mockMvc.perform(
                    post("/users")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(inputUser)))
            .andExpect(status().isBadRequest());
  }

  @SneakyThrows
  @Test
  @DisplayName("Test should return BAD_REQUEST to request create user when user email is incorrect")
  void createUser_whenUserEmailIsIncorrect_ThenReturnBadRequest() {
    String name  = "Vladimir";
    String email = "vladimir.org";

    NewUserRequest inputUser = new NewUserRequest(name, email);
    mockMvc.perform(
                    post("/users")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(inputUser)))
            .andExpect(status().isBadRequest());
  }

  @SneakyThrows
  @Test
  @DisplayName("Test should return BAD REQUEST to update request with both blank name and email")
  void updateUser_whenUserWithBlankFields_ThenReturnBadRequest() {
    int userId = 0;

    UpdateUserRequest inputUser = new UpdateUserRequest("", "");

    mockMvc.perform(
                    patch("/users/{id}", userId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(inputUser)))
            .andExpect(status().isBadRequest());
  }

}