package ru.practicum.shareit.user;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.exceptions.UserNotFoundException;

import javax.validation.ConstraintViolationException;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class UserControllerTest {

  @Autowired
  UserController userController;

  @Test
  @DirtiesContext
  void createUserSuccess() {
    UserDto userDto = UserDto.builder().withName("Ivan").withEmail("ivan@email.ru").build();
    UserDto resultUserDto = userController.createUser(userDto);
    assertEquals(1, resultUserDto.getId());
    assertEquals("Ivan", resultUserDto.getName());
    assertEquals("ivan@email.ru", resultUserDto.getEmail());
  }

  @Test
  @DirtiesContext
  void createUserFailWithoutEmail() {
    UserDto userDto = UserDto.builder().withName("Ivan").build();
    ConstraintViolationException ex = assertThrows(ConstraintViolationException.class, () -> userController.createUser(userDto));
    assertEquals("email: не должно быть пустым", ex.getMessage());
  }

  @Test
  void createUserFailWithBlankEmail() {
    UserDto userDto = UserDto.builder().withName("Ivan").withEmail("").build();
    ConstraintViolationException ex = assertThrows(ConstraintViolationException.class, () -> userController.createUser(userDto));
    assertEquals("email: не должно быть пустым", ex.getMessage());
  }

  @Test
  void createUserFailWithIncorrectEmail() {
    UserDto userDto = UserDto.builder().withName("Ivan").withEmail("aaa").build();
    ConstraintViolationException ex = assertThrows(ConstraintViolationException.class, () -> userController.createUser(userDto));
    assertEquals("email: должно иметь формат адреса электронной почты", ex.getMessage());
  }

  @Test
  void createUserFailWithoutName() {
    UserDto userDto = UserDto.builder().withEmail("ivan@email.ru").build();
    ConstraintViolationException ex = assertThrows(ConstraintViolationException.class, () -> userController.createUser(userDto));
    assertEquals("name: не должно быть пустым", ex.getMessage());
  }

  @Test
  @DirtiesContext
  void updateUserName() {
    UserDto userDto = UserDto.builder().withName("Ivan").withEmail("ivan@email.ru").build();
    UserDto resultUserDto = userController.createUser(userDto);
    String newName = "_Ivan_";
    UserDto updatedUser = UserDto.builder().withName(newName).build();
    resultUserDto = userController.updateUser(resultUserDto.getId(), updatedUser);
    assertEquals(newName, resultUserDto.getName());
  }

  @Test
  @DirtiesContext
  void testShouldFail_getUserWithUnknownId() {
    assertThrows(UserNotFoundException.class, () -> userController.getUser(100));
  }

  @Test
  @DirtiesContext
  void testShouldReturnUser() {
    UserDto userDto = UserDto.builder().withName("Ivan").withEmail("ivan@email.ru").build();
    userController.createUser(userDto);
    UserDto resUserDto = userController.getUser(1);
    assertEquals("Ivan", resUserDto.getName());
    assertEquals("ivan@email.ru", resUserDto.getEmail());
  }

  @Test
  @DirtiesContext
  void removeUserSuccess() {
    UserDto userDto = UserDto.builder().withName("Ivan").withEmail("ivan@email.ru").build();
    UserDto resultUserDto = userController.createUser(userDto);
    userController.removeUser(resultUserDto.getId());
    assertTrue(userController.getAllUsers().isEmpty());
  }

}