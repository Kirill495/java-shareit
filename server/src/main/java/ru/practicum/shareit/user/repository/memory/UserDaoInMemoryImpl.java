package ru.practicum.shareit.user.repository.memory;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.user.entity.UserEntity;
import ru.practicum.shareit.user.exceptions.UserDuplicateEmailException;
import ru.practicum.shareit.user.exceptions.UserNotFoundException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class UserDaoInMemoryImpl implements UserDao {

   private static Map<Integer, UserEntity> allUsers = new HashMap<>();
   private static Integer currentUserId = 0;

   @Override
   public List<UserEntity> getAllUsers() {
      return new ArrayList<>(allUsers.values());
   }

   @Override
   public UserEntity getUser(int userId) {
      if (!allUsers.containsKey(userId)) {
         throw new UserNotFoundException(String.format("Пользователь с идентификатором \"%d\" не существует", userId));
      }
      return allUsers.get(userId);
   }

   @Override
   public UserEntity addNewUser(UserEntity user) {

      if (allUsers.values().stream()
              .anyMatch(currentUser -> (currentUser.getEmail().equals(user.getEmail())))) {
         throw new UserDuplicateEmailException("");
      }
      user.setId(++currentUserId);
      allUsers.put(user.getId(), user);
      return user;
   }

   @Override
   public UserEntity updateUser(int userId, UserEntity user) {

      if (!allUsers.containsKey(userId)) {
         throw new UserNotFoundException(String.format("Пользователь с идентификатором \"%d\" не существует", userId));
      }
      if (allUsers.values().stream()
              .anyMatch(currentUser -> (currentUser.getEmail().equals(user.getEmail()) && currentUser.getId() != userId))) {
         throw new UserDuplicateEmailException("");
      }
      UserEntity savedUser = allUsers.get(userId);
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
