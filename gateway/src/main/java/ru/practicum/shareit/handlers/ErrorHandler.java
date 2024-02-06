package ru.practicum.shareit.handlers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.shareit.booking.exceptions.CreateNewBookingException;
import ru.practicum.shareit.booking.validators.ValidationErrorResponse;
import ru.practicum.shareit.booking.validators.Violation;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

   @ExceptionHandler(MethodArgumentNotValidException.class)
   @ResponseStatus(HttpStatus.BAD_REQUEST)
   @ResponseBody
   public ValidationErrorResponse handleMethodArgumentNotValidException(
           MethodArgumentNotValidException e
   ) {
      final List<Violation> violations = e.getBindingResult().getFieldErrors().stream()
              .map(error -> new Violation(error.getField(), error.getDefaultMessage()))
              .collect(Collectors.toList());
      return new ValidationErrorResponse(violations);
   }

   @ExceptionHandler(IllegalArgumentException.class)
   @ResponseStatus(HttpStatus.BAD_REQUEST)
   public Map<String, String> handleIllegalArgumentException(
           final IllegalArgumentException exception) {
      log.debug("Получен статус {}", HttpStatus.BAD_REQUEST, exception);
      return Map.of("error", "Unknown state: UNSUPPORTED_STATUS");
   }

   @ExceptionHandler(CreateNewBookingException.class)
   @ResponseStatus(HttpStatus.BAD_REQUEST)
   public Map<String, String> handleCreateNewBookingException(
           final CreateNewBookingException exception) {
      log.debug("Получен статус {}", HttpStatus.BAD_REQUEST, exception);
      return Map.of("error", exception.getMessage());
   }

   @ExceptionHandler(RuntimeException.class)
   @ResponseStatus(HttpStatus.BAD_REQUEST)
   public Map<String, String> handleRuntimeException(
           final RuntimeException exception) {
      log.debug("Получен статус {}", HttpStatus.BAD_REQUEST, exception);
      return Map.of("error", exception.getMessage());
   }
}
