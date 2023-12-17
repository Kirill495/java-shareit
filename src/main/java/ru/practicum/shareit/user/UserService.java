package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.dao.UserDao;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.validation.CreateNewUserInfo;
import ru.practicum.shareit.user.validation.UpdateUserInfo;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {
   private final UserDao repository;

   public List<UserDto> getAllUsers() {
      return repository.getAllUsers().stream().map(UserMapper::toUserDto).collect(Collectors.toList());
   }

   public UserDto getUser(int userId) {
      return UserMapper.toUserDto(repository.getUser(userId));
   }

   public UserDto createNewUser(UserDto userDto) {
      validateUserInput(userDto, CreateNewUserInfo.class);
      User user = UserMapper.toUser(userDto);
      return UserMapper.toUserDto(repository.addNewUser(user));
   }

   public UserDto updateUser(int userId, UserDto userDto) {
      validateUserInput(userDto, UpdateUserInfo.class);
      User user = UserMapper.toUser(userDto);
      return UserMapper.toUserDto(repository.updateUser(userId, user));
   }

   public void removeUser(int userId) {
      repository.removeUser(userId);
   }

   private <T> void validateUserInput(UserDto userDto, Class<T> className) {
      Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
      Set<ConstraintViolation<UserDto>> violations = validator.validate(userDto, className);
      if (!violations.isEmpty()) {
         StringBuilder sb = new StringBuilder();
         for (ConstraintViolation<UserDto> violation : violations) {
            sb.append(violation.getPropertyPath());
            sb.append(": ");
            sb.append(violation.getMessage());
         }
         throw new ConstraintViolationException(sb.toString(), violations);
      }
   }
}
