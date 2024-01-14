package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.exceptions.CreateNewUserException;
import ru.practicum.shareit.user.exceptions.UpdateUserException;
import ru.practicum.shareit.user.exceptions.UserDuplicateEmailException;
import ru.practicum.shareit.user.exceptions.UserNotFoundException;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

   private final UserRepository userRepository;
   private final UserMapper userMapper;

   public List<User> getAllUsers() {
      return userMapper.toModel(userRepository.findAll());
   }

   public User getUser(int userId) {
      return userMapper
              .toModel(userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId)));
   }

   public User createNewUser(User user) {
      try {
         return userMapper
                 .toModel(userRepository.save(userMapper.toEntity(user)));
      } catch (DataIntegrityViolationException exception) {
         if (userRepository.existsByEmail(user.getEmail())) {
            throw new UserDuplicateEmailException(user);
         }
         throw new CreateNewUserException(exception);
      }
   }

   public User updateUser(int userId, User inputUser) {
      User savedUser = userMapper
              .toModel(userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId)));
      if (inputUser.getName() != null) {
         savedUser.setName(inputUser.getName());
      }
      if (inputUser.getEmail() != null) {
         savedUser.setEmail(inputUser.getEmail());
      }

      try {
         return userMapper.toModel(userRepository.save(userMapper.toEntity(savedUser)));
      } catch (DataIntegrityViolationException exception) {
         if (userRepository.existsByEmail(inputUser.getEmail())) {
            throw new UserDuplicateEmailException(inputUser);
         }
         throw new UpdateUserException(exception);
      }
   }

   public void removeUser(int userId) {
      try {
         userRepository.deleteById(userId);
      } catch (EmptyResultDataAccessException e) {
         throw new UserNotFoundException(userId);
      }
   }

}