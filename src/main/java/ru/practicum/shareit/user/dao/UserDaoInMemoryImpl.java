package ru.practicum.shareit.user.dao;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.exceptions.UserDuplicateEmailException;
import ru.practicum.shareit.user.exceptions.UserNotFoundException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class UserDaoInMemoryImpl implements UserDao {

   private static Map<Integer, User> allUsers = new HashMap<>();
   private static Integer currentUserId = 0;

   @Override
   public List<User> getAllUsers() {
      return new ArrayList<>(allUsers.values());
   }

   @Override
   public User getUser(int userId) {
      if (!allUsers.containsKey(userId)) {
         throw new UserNotFoundException(String.format("Пользователь с идентификатором \"%d\" не существует", userId));
      }
      return allUsers.get(userId);
   }

   @Override
   public User addNewUser(User user) {

      if (allUsers.values().stream()
              .anyMatch(currentUser -> (currentUser.getEmail().equals(user.getEmail())))) {
         throw new UserDuplicateEmailException(
                 String.format("Пользователь с email \"%s\" уже существует", user.getEmail()));
      }
      user.setId(++currentUserId);
      allUsers.put(user.getId(), user);
      return user;
   }

   @Override
   public User updateUser(int userId, User user) {

      if (!allUsers.containsKey(userId)) {
         throw new UserNotFoundException(String.format("Пользователь с идентификатором \"%d\" не существует", userId));
      }
      if (allUsers.values().stream()
              .anyMatch(currentUser -> (currentUser.getEmail().equals(user.getEmail()) && currentUser.getId() != userId))) {
         throw new UserDuplicateEmailException(
                 String.format("Пользователь с email \"%s\" уже существует", user.getEmail()));
      }
      User savedUser = allUsers.get(userId);
      if (user.getEmail() != null) {
         savedUser.setEmail(user.getEmail());
      }
      if (user.getName() != null) {
         savedUser.setName(user.getName());
      }
      return savedUser;
   }

   @Override
   public void removeUser(int userId) {
      if (!allUsers.containsKey(userId)) {
         throw new UserNotFoundException(String.format("Пользователь с идентификатором \"%d\" не существует", userId));
      }
      allUsers.remove(userId);
   }
}
