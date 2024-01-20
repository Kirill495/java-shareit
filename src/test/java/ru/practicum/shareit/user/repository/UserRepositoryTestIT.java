package ru.practicum.shareit.user.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.practicum.shareit.user.entity.UserEntity;

import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
class UserRepositoryTestIT {

  @Autowired
  private UserRepository userRepository;

  @BeforeEach
  void setUp() {
    userRepository.save(new UserEntity(1, "Oleg", "oleg@email.ru"));
  }

  @AfterEach
  void tearDown() {
    userRepository.delete(new UserEntity(1, "Oleg", "oleg@email.ru"));
  }

  @Test
  void existsByEmail() {
    boolean result = userRepository.existsByEmail("oleg@email.ru");
    assertTrue(result);
  }
}