package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.user.dto.NewUserRequest;
import ru.practicum.shareit.user.dto.UpdateUserRequest;

import javax.validation.Valid;
import javax.validation.constraints.Positive;

@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
@Validated
public class UserController {

  private final UserClient userClient;

  @GetMapping
  public ResponseEntity<Object> getAllUsers() {
    return userClient.getAllUsers();
  }

  @GetMapping("/{userId}")
  public ResponseEntity<Object> getUser(@PathVariable @Positive Integer userId) {
    return userClient.getUser(userId);
  }

  @PostMapping
  public ResponseEntity<Object> createUser(@Valid @RequestBody NewUserRequest request) {
    return userClient.addUser(request);
  }

  @PatchMapping("/{userId}")
  public ResponseEntity<Object> updateUser(
          @PathVariable @Positive Integer userId,
          @Valid @RequestBody UpdateUserRequest request) {
    return userClient.updateUser(userId, request);
  }

  @DeleteMapping("/{userId}")
  public ResponseEntity<Object> removeUser(@PathVariable @Positive Integer userId) {
    return userClient.removeUser(userId);
  }
}
