package ru.practicum.shareit.user.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.user.dto.OutputUser;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

  @Mock
  private UserService userService;
  @Mock
  private UserMapper userMapper;
  @InjectMocks
  private UserController userController;

  @Test
  void getAllUsers_whenInvoked_thenReturnBodyWithUsersCollection() {

    List<User> users = List.of(new User(1, "Victor", "v@email.ru"));
    List<OutputUser> outputUsersControl = List.of(new OutputUser(1, "Victor", "v@email.ru"));
    Mockito.when(userService.getAllUsers()).thenReturn(users);
    Mockito.when(userMapper.toOutputUser(users)).thenReturn(outputUsersControl);

    List<OutputUser> outputUserList = userController.getAllUsers();

    assertEquals(1, outputUserList.size());
    assertEquals(outputUsersControl, outputUserList);
  }

}