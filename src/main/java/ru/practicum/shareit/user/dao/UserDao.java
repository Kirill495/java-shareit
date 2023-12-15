package ru.practicum.shareit.user.dao;

import ru.practicum.shareit.user.User;

import java.util.List;

public interface UserDao {
   List<User> getAllUsers();

   User getUser(int userId);

   User addNewUser(User user);

   User updateUser(int userId, User user);

   void removeUser(int userId);
}
