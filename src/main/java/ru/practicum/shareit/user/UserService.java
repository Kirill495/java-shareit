package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.exceptions.UserDuplicateEmailException;
import ru.practicum.shareit.user.exceptions.UserNotFoundException;
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

//   private final UserDao repository;
   @Autowired
   private final UserRepository userRepository;

   public List<UserDto> getAllUsers() {
      return userRepository.findAll().stream().map(UserMapper::toUserDto).collect(Collectors.toList());
//      return repository.getAllUsers().stream().map(UserMapper::toUserDto).collect(Collectors.toList());
   }

   public UserDto getUser(int userId) {
      return UserMapper.toUserDto(userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId)));
   }

   public UserDto createNewUser(UserDto userDto) {
      validateUserInput(userDto, CreateNewUserInfo.class);
      User user = UserMapper.toUser(userDto);

      User savedUser = null;
      try {
         savedUser = userRepository.save(user);
      } catch (DataIntegrityViolationException exception) {
         if (userRepository.existsByEmail(user.getEmail())) {
            throw new UserDuplicateEmailException(user);
         }
      }
      return UserMapper.toUserDto(savedUser);
   }

   public UserDto updateUser(int userId, UserDto userDto) {

      validateUserInput(userDto, UpdateUserInfo.class);
      User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
      if (userDto.getName() != null) {
         user.setName(userDto.getName());
      }
      if (userDto.getEmail() != null) {
         user.setEmail(userDto.getEmail());
      }
      User updatedUser = null;
      try {
         updatedUser = userRepository.save(user);
      } catch (DataIntegrityViolationException exception) {
         if (userRepository.existsByEmail(user.getEmail())) {
            throw new UserDuplicateEmailException(user);
         }
      }
      return UserMapper.toUserDto(updatedUser);
   }

   public void removeUser(int userId) {
      try {
         userRepository.deleteById(userId);
      } catch (EmptyResultDataAccessException e) {
         throw new UserNotFoundException(userId);
      }
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
