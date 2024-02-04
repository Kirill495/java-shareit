package ru.practicum.shareit.handlers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class ErrorHandler {

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

   @ExceptionHandler(IllegalArgumentException.class)
   @ResponseStatus(HttpStatus.BAD_REQUEST)
   public Map<String, String> handleIllegalArgumentException(
           final IllegalArgumentException exception) {
      log.debug("Получен статус {}", HttpStatus.BAD_REQUEST, exception);
      return Map.of("error", "Unknown state: UNSUPPORTED_STATUS");
   }

}
