package ru.practicum.shareit.user.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.user.exceptions.UserDuplicateEmailException;
import ru.practicum.shareit.user.exceptions.UserNotFoundException;
import ru.practicum.shareit.user.model.User;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class UserDaoTest {

  UserDao userDao;

  @BeforeEach
  void setUp() {
    userDao = new UserDaoInMemoryImpl();
    User user1 = User.builder().withEmail("user@email.org").withName("user1").build();
    userDao.addNewUser(user1);
  }

  @Test
  void addNewUserWithTheSameEmail() {
    final User user2 = User.builder().withEmail("user@email.org").withName("user2").build();
    assertThrows(UserDuplicateEmailException.class, () -> userDao.addNewUser(user2));
  }

  @Test
  void updateUserWithNonExistentIdShouldFail() {
    User user = User.builder().withEmail("userUpdate@email.org").withName("user2").build();
    assertThrows(UserNotFoundException.class, () -> userDao.updateUser(100, user));
  }

  @Test
  void updateUserWithSameEmailShouldFail() {
    User user = User.builder().withEmail("userUpdate@email.org").withName("user2").build();
    User newUser = userDao.addNewUser(user);
    UserDuplicateEmailException ex = assertThrows(UserDuplicateEmailException.class,
            () -> userDao.updateUser(newUser.getId(), User.builder().withEmail("user@email.org").build()));
    assertEquals("Пользователь с email \"user@email.org\" уже существует", ex.getMessage());
  }

  @Test
  void updateUserWithNotBlankEmail() {
    User updatedUser = userDao.updateUser(1, User.builder().withEmail("_user_@email.org").build());
    assertEquals("_user_@email.org", updatedUser.getEmail());
  }

  @Test
  void removeUserWithUnknownId() {
    assertThrows(UserNotFoundException.class, () -> userDao.removeUser(100));
  }

}