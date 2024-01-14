package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface UserService {

  List<User> getAllUsers();

  User getUser(int userId);

  User createNewUser(User user);

  User updateUser(int userId, User inputUser);

  void removeUser(int userId);
}
