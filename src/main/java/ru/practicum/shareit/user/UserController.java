package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
public class UserController {

   private final UserService service;

   @GetMapping
   public List<UserDto> getAllUsers() {
      return service.getAllUsers();
   }

   @GetMapping("/{userId}")
   public UserDto getUser(@PathVariable Integer userId) {
      return service.getUser(userId);
   }

   @PostMapping
   public UserDto createUser(@RequestBody UserDto userDto) {
      return service.createNewUser(userDto);
   }

   @PatchMapping("/{userId}")
   public UserDto updateUser(@PathVariable Integer userId, @RequestBody UserDto userDto) {
      return service.updateUser(userId, userDto);
   }

   @DeleteMapping("/{userId}")
   public void removeUser(@PathVariable Integer userId) {
      service.removeUser(userId);
   }

}
