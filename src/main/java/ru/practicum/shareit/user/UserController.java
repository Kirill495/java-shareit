package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
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
