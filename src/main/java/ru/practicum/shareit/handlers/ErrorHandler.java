package ru.practicum.shareit.handlers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.shareit.item.exceptions.UserItemNotFoundException;
import ru.practicum.shareit.user.exceptions.UserDuplicateEmailException;
import ru.practicum.shareit.user.exceptions.UserNotFoundException;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class ErrorHandler {

   @ExceptionHandler(UserNotFoundException.class)
   @ResponseStatus(HttpStatus.NOT_FOUND)
   public Map<String, String> handleUserNotFoundException(final UserNotFoundException exception) {
      log.debug("Получен статус {}", HttpStatus.NOT_FOUND, exception);
      return Map.of("error", exception.getMessage());
   }

   @ExceptionHandler(UserItemNotFoundException.class)
   @ResponseStatus(HttpStatus.NOT_FOUND)
   public Map<String, String> handleUserItemNotFoundException(final UserItemNotFoundException exception) {
      log.debug("Получен статус {}", HttpStatus.NOT_FOUND, exception);
      return Map.of("error", exception.getMessage());
   }

   @ExceptionHandler(UserDuplicateEmailException.class)
   @ResponseStatus(HttpStatus.CONFLICT)
   public Map<String, String> handleDuplicateEmailException(final UserDuplicateEmailException exception) {
      log.debug("Получен статус {}", HttpStatus.CONFLICT, exception);
      return Map.of("error", exception.getMessage());
   }

   @ExceptionHandler(ConstraintViolationException.class)
   @ResponseStatus(HttpStatus.BAD_REQUEST)
   public Map<String, String> handleConstraintViolationException(
           final ConstraintViolationException exception) {
      log.debug("Получен статус {}", HttpStatus.BAD_REQUEST, exception);
      Map<String, String> error = new HashMap<>();
      for (ConstraintViolation<?> violation : exception.getConstraintViolations()) {
         error.put(violation.getPropertyPath().toString(), violation.getMessage());
      }
      return error;
   }

}
