package ru.practicum.shareit.user.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.user.entity.UserEntity;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

  @Mock
  private UserMapper userMapper;

  @Mock
  private UserRepository userRepository;

  @Captor
  private ArgumentCaptor<UserEntity> userEntityArgumentCaptor;

  @Captor
  private ArgumentCaptor<UserEntity> finalUserEntityArgumentCaptor;

  @Captor
  private ArgumentCaptor<User> userModelArgumentCaptor;

  @InjectMocks
  private UserServiceImpl userService;

  @Test
  void updateUser_whenUserFound_thenUpdateOnlyNonNullFields() {
    int userId = 1;
    User oldUser = new User(userId, "Semen", "semen@email.ru");
    UserEntity oldUserEntity = new UserEntity(userId, "Semen", "semen@email.ru");
    User newUser = new User(null, "Alexey", null);
    User updatedUser = new User(userId, "Alexey", "semen@email.ru");
    UserEntity updatedUserEntity = new UserEntity(userId, "Alexey", "semen@email.ru");

    when(userRepository.findById(userId)).thenReturn(Optional.of(oldUserEntity));
    when(userMapper.toEntity(updatedUser)).thenReturn(updatedUserEntity);
    when(userMapper.toModel(oldUserEntity)).thenReturn(oldUser);

    User u = userService.updateUser(userId, newUser);

    verify(userRepository, times(1)).save(userEntityArgumentCaptor.capture());

    UserEntity savedUser = userEntityArgumentCaptor.getValue();

    assertEquals(updatedUserEntity, savedUser);

  }
}
