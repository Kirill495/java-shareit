package ru.practicum.shareit.user.controller;

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
import ru.practicum.shareit.user.dto.OutputUser;
import ru.practicum.shareit.user.dto.UpdateUserRequest;
import ru.practicum.shareit.user.exceptions.UserNotFoundException;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserServiceImpl;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = UserController.class)
@ActiveProfiles(profiles = {"test"})
class UserControllerIntegrationTest {

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
  @DisplayName("Test should return new user and status OK for GET-request")
  void getUser_whenUserExists_ThenReturnUser() {

    int userId = 0;
    User user = new User(userId, "Anna", "anna@email.org");
    OutputUser output = new OutputUser(userId, "Anna", "anna@email.org");

    when(userService.getUser(userId)).thenReturn(user);
    when(userMapper.toOutputUser(user)).thenReturn(output);
    mockMvc.perform(get("/users/{id}", userId))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(userId))
            .andExpect(jsonPath("$.name").value("Anna"))
            .andExpect(jsonPath("$.email").value("anna@email.org"));

    verify(userService, times(1)).getUser(userId);
    verify(userMapper, times(1)).toOutputUser(user);
  }

  @Test
  @DisplayName("Test should return status NOT_FOUND for GET-request")
  void getUser_whenUserNotExists_ThenReturnStatusNotFound() throws Exception {

    int userId = 0;
    when(userService.getUser(userId)).thenThrow(new UserNotFoundException(userId));

    mockMvc.perform(get("/users/{id}", userId))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.error").value("Пользователь с id \"0\" не существует"));
  }

  @Test
  @DisplayName("Test should return new User and status CREATED")
  void createUser_whenUserNotExists_ThenReturnNewUser() throws Exception {
    int userId = 0;
    String name = "Vladimir";
    String email = "vladimir@email.org";

    NewUserRequest inputUser = new NewUserRequest(name, email);
    User userToSave = new User(null, name, email);
    User savedUser = new User(userId, name, email);
    OutputUser savedUserResult = new OutputUser(userId, name, email);

    when(userMapper.toModel(inputUser)).thenReturn(userToSave);
    when(userService.createNewUser(userToSave)).thenReturn(savedUser);
    when(userMapper.toOutputUser(savedUser)).thenReturn(new OutputUser(userId, name, email));

    mockMvc.perform(
                    post("/users")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(inputUser)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").value(savedUserResult.getId()))
            .andExpect(jsonPath("$.name").value(savedUserResult.getName()))
            .andExpect(jsonPath("$.email").value(savedUserResult.getEmail()));

    verify(userService, times(1)).createNewUser(userToSave);
    verify(userMapper, times(1)).toModel(inputUser);
    verify(userMapper, times(1)).toOutputUser(savedUser);
  }

  @Test
  @DisplayName("Test should return BAD_REQUEST for create user request when user name is blank")
  void createUser_whenUserNameIsEmpty_ThenReturnBadRequest() throws Exception {
    String email = "vladimir@email.org";

    NewUserRequest inputUser = new NewUserRequest("", email);
    mockMvc.perform(
                    post("/users")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(inputUser)))
            .andExpect(status().isBadRequest());
  }

  @Test
  @DisplayName("Test should return BAD_REQUEST for crate user request when user name is null")
  void createUser_whenUserNameIsNull_ThenReturnBadRequest() throws Exception {
    String email = "vladimir@email.org";

    NewUserRequest inputUser = new NewUserRequest(null, email);
    mockMvc.perform(
                    post("/users")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(inputUser)))
            .andExpect(status().isBadRequest());
  }

  @Test
  @DisplayName("Test should return BAD_REQUEST for request create user when user email is blank")
  void createUser_whenUserEmailIsEmpty_ThenReturnBadRequest() throws Exception {
    String name = "Vladimir";

    NewUserRequest inputUser = new NewUserRequest(name, "");
    mockMvc.perform(
                    post("/users")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(inputUser)))
            .andExpect(status().isBadRequest());
  }

  @Test
  @DisplayName("Test should return BAD_REQUEST to request create user when user email is incorrect")
  void createUser_whenUserEmailIsIncorrect_ThenReturnBadRequest() throws Exception {
    String name  = "Vladimir";
    String email = "vladimir.org";

    NewUserRequest inputUser = new NewUserRequest(name, email);
    mockMvc.perform(
                    post("/users")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(inputUser)))
            .andExpect(status().isBadRequest());
  }

  @Test
  @DisplayName("Test should return status OK and new updated user to update request with correct input")
  void updateUser_whenUserIsCorrect_ThenReturnNewUser() throws Exception {

    int userId = 0;
    String name = "Vladimir";
    String email = "vladimir@email.org";

    UpdateUserRequest inputUser = new UpdateUserRequest(name, email);
    User userToSave = new User(null, name, email);
    User savedUser = new User(userId, name, email);
    OutputUser savedUserResult = new OutputUser(userId, name, email);

    when(userMapper.toModel(inputUser)).thenReturn(userToSave);
    when(userService.updateUser(userId, userToSave)).thenReturn(savedUser);
    when(userMapper.toOutputUser(savedUser)).thenReturn(savedUserResult);

    mockMvc.perform(
                    patch("/users/{id}", userId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(inputUser)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(savedUserResult.getId()))
            .andExpect(jsonPath("$.name").value(savedUserResult.getName()))
            .andExpect(jsonPath("$.email").value(savedUserResult.getEmail()));
  }

  @Test
  @DisplayName("Test should return BAD REQUEST to update request with both blank name and email")
  void updateUser_whenUserWithBlankFields_ThenReturnBadRequest() throws Exception {
    int userId = 0;

    UpdateUserRequest inputUser = new UpdateUserRequest("", "");

    mockMvc.perform(
                    patch("/users/{id}", userId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(inputUser)))
            .andExpect(status().isBadRequest());
  }

  @Test
  @DisplayName("Test should return OK for remove request and user exists")
  void removeUser_whenUserExists_thenReturnOk() throws Exception {
    int userId = 0;
    doNothing().when(userService).removeUser(userId);

    mockMvc.perform(delete("/users/{id}", userId))
            .andExpect(status().isOk());
  }

  @Test
  @DisplayName("Test should return NOT_FOUND for remove request when userId is unknown")
  void removeUser_whenUserNotExists_thenReturnNotFound() throws Exception {
    int userId = 0;
    doThrow(new UserNotFoundException(userId)).when(userService).removeUser(userId);

    mockMvc.perform(delete("/users/{id}", userId))
            .andExpect(status().isNotFound());
  }
}