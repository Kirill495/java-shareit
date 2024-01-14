package ru.practicum.shareit.user.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.user.dto.NewUserRequest;
import ru.practicum.shareit.user.dto.OutputUser;
import ru.practicum.shareit.user.dto.UpdateUserRequest;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.service.UserService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
public class UserController {

   private final UserService service;
   private final UserMapper userMapper;

   @GetMapping
   public List<OutputUser> getAllUsers() {
      return userMapper.toOutputUser(service.getAllUsers());
   }

   @GetMapping("/{userId}")
   public OutputUser getUser(@PathVariable Integer userId) {
      return userMapper
              .toOutputUser(service.getUser(userId));
   }

   @PostMapping
   public OutputUser createUser(@RequestBody @Valid NewUserRequest request) {
      return userMapper
              .toOutputUser(service.createNewUser(userMapper.toModel(request)));
   }

   @PatchMapping("/{userId}")
   public OutputUser updateUser(@PathVariable Integer userId, @RequestBody @Valid UpdateUserRequest request) {
      return userMapper
              .toOutputUser(service.updateUser(userId, userMapper.toModel(request)));
   }

   @DeleteMapping("/{userId}")
   public void removeUser(@PathVariable Integer userId) {
      service.removeUser(userId);
   }

}
