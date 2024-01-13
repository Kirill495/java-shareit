package ru.practicum.shareit.user.repository.memory;

import ru.practicum.shareit.user.entity.UserEntity;

import java.util.List;

public interface UserDao {
   List<UserEntity> getAllUsers();

   UserEntity getUser(int userId);

   UserEntity addNewUser(UserEntity user);

   UserEntity updateUser(int userId, UserEntity user);

   void removeUser(int userId);
}
