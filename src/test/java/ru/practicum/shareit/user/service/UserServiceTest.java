package ru.practicum.shareit.user.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ActiveProfiles;
import ru.practicum.shareit.user.entity.UserEntity;
import ru.practicum.shareit.user.exceptions.CreateNewUserException;
import ru.practicum.shareit.user.exceptions.UserDuplicateEmailException;
import ru.practicum.shareit.user.exceptions.UserNotFoundException;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles(profiles = {"test"})
public class UserServiceTest {

  @Mock
  private UserMapper userMapper;

  @Mock
  private UserRepository userRepository;

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
  void getAllUsers_whenRepositoryIsEmpty_thenReturnEmptyList() {
    when(userMapper.toModel(Collections.emptyList())).thenReturn(Collections.emptyList());
    when(userRepository.findAll()).thenReturn(Collections.emptyList());

    userService.getAllUsers();

  }

  @Test
  void updateUser_whenUserFound_thenUpdateOnlyNonNullFields() {
    int userId = 1;
    User oldUser = new User(userId, "Semen", "semen@email.ru");
    UserEntity oldUserEntity = new UserEntity(userId, "Semen", "semen@email.ru");
    User newUser = new User(null, "Alexey", null);
    User updatedUser = new User(userId, "Alexey", "semen@email.ru");
    UserEntity updatedUserEntity = new UserEntity(userId, "Alexey", "semen@email.ru");

    Mockito.when(userRepository.findById(userId)).thenReturn(Optional.of(oldUserEntity));
    Mockito.when(userMapper.toEntity(updatedUser)).thenReturn(updatedUserEntity);
    Mockito.when(userMapper.toModel(oldUserEntity)).thenReturn(oldUser);

    userService.updateUser(userId, newUser);

    Mockito.verify(userRepository, Mockito.times(1)).save(userEntityArgumentCaptor.capture());
    UserEntity savedUser = userEntityArgumentCaptor.getValue();

    assertEquals(updatedUserEntity, savedUser);
  }

  @Test
  void updateUser_whenUserNotFound_ThenThrowUserNotFoundException() {
    int userId = 1;

    Mockito.when(userRepository.findById(userId)).thenReturn(Optional.empty());

    UserNotFoundException ex = assertThrows(UserNotFoundException.class, () -> userService.updateUser(userId, new User(null, "Herman", "herman@email.org")));

    assertEquals("Пользователь с id \"1\" не существует", ex.getMessage());
    Mockito.verify(userRepository, Mockito.times(1)).findById(userId);
    Mockito.verify(userRepository, Mockito.never()).save(Mockito.any());
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

  @Test
  void createNewUser_whenUserDoesNotExist_thenNewUser() {
    int userId = 1;
    User inputUser = new User(null, "Roman", "roman@email.ru");
    UserEntity inputUserEntity = new UserEntity(null, "Roman", "roman@email.ru");

    User outputUser = new User(userId, "Roman", "roman@email.ru");
    UserEntity outputUserEntity = new UserEntity(userId, "Roman", "roman@email.ru");

    Mockito.when(userMapper.toEntity(inputUser)).thenReturn(inputUserEntity);
    Mockito.when(userMapper.toModel(outputUserEntity)).thenReturn(outputUser);
    Mockito.when(userRepository.save(inputUserEntity)).thenReturn(outputUserEntity);

    User result = userService.createNewUser(inputUser);

    assertEquals(outputUser, result);
    Mockito.verify(userRepository, Mockito.times(1)).save(inputUserEntity);
    Mockito.verify(userRepository, Mockito.never()).existsByEmail(Mockito.any(String.class));
  }

  @Test
  void createNewUser_whenUserWithTheSameEmailExists_thenThrowUserDuplicateException() {

    User inputUser = new User(null, "Roman", "roman@email.ru");
    UserEntity inputUserEntity = new UserEntity(null, "Roman", "roman@email.ru");
    Mockito.when(userMapper.toEntity(inputUser)).thenReturn(inputUserEntity);
    Mockito.when(userRepository.save(inputUserEntity)).thenThrow(DataIntegrityViolationException.class);
    Mockito.when(userRepository.existsByEmail(inputUser.getEmail())).thenReturn(true);

    UserDuplicateEmailException ex = assertThrows(
            UserDuplicateEmailException.class,
            () -> userService.createNewUser(inputUser));

    assertEquals("Пользователь с email \"roman@email.ru\" уже существует", ex.getMessage());
    Mockito.verify(userRepository, Mockito.times(1)).save(inputUserEntity);
    Mockito.verify(userRepository, Mockito.times(1)).existsByEmail(inputUser.getEmail());
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
  void createNewUser_whenUserIsIncorrect_thenThrowCreateNewUserException() {
    UserEntity userEntity = new UserEntity(1, "Petr", "petr@email.ru");
    User userModel = new User(1, "Petr", "petr@email.ru");

    when(userRepository.save(userEntity)).thenThrow(new DataIntegrityViolationException(""));
    when(userRepository.existsByEmail(userModel.getEmail())).thenReturn(false);
    when(userMapper.toEntity(userModel)).thenReturn(userEntity);

    assertThrows(CreateNewUserException.class, () -> userService.createNewUser(userModel));

    verify(userRepository, times(1)).save(userEntity);
    verify(userRepository, times(1)).existsByEmail(userModel.getEmail());
  }
}
