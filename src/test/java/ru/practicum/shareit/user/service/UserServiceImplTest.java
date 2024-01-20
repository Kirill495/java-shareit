package ru.practicum.shareit.user.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import ru.practicum.shareit.user.entity.UserEntity;
import ru.practicum.shareit.user.exceptions.UserDuplicateEmailException;
import ru.practicum.shareit.user.exceptions.UserNotFoundException;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

  @Mock
  private UserRepository userRepository;
  @Mock
  private UserMapper userMapper;

  @Captor
  private ArgumentCaptor<UserEntity> userEntityArgumentCaptor;

  @InjectMocks
  private UserServiceImpl userService;

  @Test
  void getUser_whenUserFound_thenReturnUser() {

    int userId = 0;
    UserEntity userEntity = new UserEntity(1, "Victor", "victor@email.ru");
    User controlValue = new User(1, "Victor", "victor@email.ru");
    when(userRepository.findById(userId)).thenReturn(Optional.of(userEntity));
    when(userMapper.toModel(userEntity)).thenReturn(controlValue);

    User user = userService.getUser(userId);

    assertEquals(controlValue, user);
  }

  @Test
  void getUser_whenUserIsNotFound_thenUserNotFoundException() {

    int userId = 0;
    UserEntity userEntity = new UserEntity(1, "Victor", "victor@email.ru");
    when(userRepository.findById(userId)).thenReturn(Optional.empty());

    UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> userService.getUser(userId));
    assertEquals("Пользователь с id \"0\" не существует", exception.getMessage());
  }

  @Test
  void createNewUser_whenUserEmailValid_thenSaveUser() {
    UserEntity userEntity = new UserEntity(1, "Petr", "petr@email.ru");
    User userModel = new User(1, "Petr", "petr@email.ru");
    when(userRepository.save(userEntity)).thenReturn(userEntity);
    when(userMapper.toEntity(userModel)).thenReturn(userEntity);
    when(userMapper.toModel(userEntity)).thenReturn(userModel);

    assertEquals(userModel, userService.createNewUser(userModel));
    verify(userRepository).save(userEntity);
  }

  @Test
  void createNewUser_whenUserEmailNotValid_thenNotSaveUser() {
    UserEntity userEntity = new UserEntity(1, "Petr", "petr@email.ru");
    User userModel = new User(1, "Petr", "petr@email.ru");

    when(userRepository.save(userEntity)).thenThrow(new DataIntegrityViolationException(""));
    when(userRepository.existsByEmail(userModel.getEmail())).thenReturn(true);
    when(userMapper.toEntity(userModel)).thenReturn(userEntity);

    UserDuplicateEmailException exception = assertThrows(UserDuplicateEmailException.class,
            () -> userService.createNewUser(userModel));

    assertEquals("Пользователь с email \"petr@email.ru\" уже существует", exception.getMessage());
    verify(userRepository).save(userEntity);
    verify(userRepository).existsByEmail(userModel.getEmail());
  }


  @Test
  void updateUser_whenUserFound_thenUpdateOnlyAvailableFields() {
    int userId = 1;
    User oldUser = new User(userId, "Semen", "semen@email.ru");
    UserEntity oldUserEntity = new UserEntity(userId, "Semen", "semen@email.ru");
    User newUser = new User(null, "Alexey", null);

    when(userRepository.findById(userId)).thenReturn(Optional.of(oldUserEntity));
    when(userMapper.toModel(oldUserEntity)).thenReturn(oldUser);
    when(userMapper.toEntity(new User(userId, "Alexey", "semen@email.ru")))
            .thenReturn(new UserEntity(userId, "Alexey", "semen@email.ru"));

    userService.updateUser(userId, newUser);

    verify(userRepository).save(userEntityArgumentCaptor.capture());
    UserEntity savedUser = userEntityArgumentCaptor.getValue();

    assertEquals(1, savedUser.getId());
    assertEquals("Alexey", savedUser.getName());
    assertEquals("semen@email.ru", savedUser.getEmail());
  }
}