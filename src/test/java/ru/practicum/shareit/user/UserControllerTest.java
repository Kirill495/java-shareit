package ru.practicum.shareit.user;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.user.dto.UserDto;

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
    assertEquals("email: не должно равняться null", ex.getMessage());
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
}