package ru.practicum.shareit.handlers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.shareit.booking.exceptions.BookingNotFoundException;
import ru.practicum.shareit.booking.exceptions.BookingSetStatusByOwnerException;
import ru.practicum.shareit.booking.exceptions.CreateBookingByOwnerException;
import ru.practicum.shareit.booking.exceptions.CreateBookingForUnavailableItemException;
import ru.practicum.shareit.booking.exceptions.UnknownBookingStateException;
import ru.practicum.shareit.booking.exceptions.UserIsNotOwnerOfBookedItem;
import ru.practicum.shareit.booking.exceptions.NoBookingsToItemException;
import ru.practicum.shareit.item.exceptions.ItemNotFoundException;
import ru.practicum.shareit.user.exceptions.CreateNewUserException;
import ru.practicum.shareit.user.exceptions.UpdateUserException;
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

   @ExceptionHandler(ItemNotFoundException.class)
   @ResponseStatus(HttpStatus.NOT_FOUND)
   public Map<String, String> handleItemNotFoundException(final ItemNotFoundException exception) {
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

   @ExceptionHandler(BookingNotFoundException.class)
   @ResponseStatus(HttpStatus.NOT_FOUND)
   public Map<String, String> handleBookingNotFoundException(final BookingNotFoundException exception) {
      log.debug("Получен статус {}", HttpStatus.NOT_FOUND, exception);
      return Map.of("error", exception.getMessage());
   }

   @ExceptionHandler(CreateBookingForUnavailableItemException.class)
   @ResponseStatus(HttpStatus.BAD_REQUEST)
   public Map<String, String> handleCreateBookingForUnavailableItemException(
           final CreateBookingForUnavailableItemException exception) {
      log.debug("Получен статус {}", HttpStatus.BAD_REQUEST, exception);
      return Map.of("error", exception.getMessage());
   }

   @ExceptionHandler(UnknownBookingStateException.class)
   @ResponseStatus(HttpStatus.BAD_REQUEST)
   public Map<String, String> handleUnknownBookingStateException(
           final UnknownBookingStateException exception) {
      log.debug("Получен статус {}", HttpStatus.BAD_REQUEST, exception);
      return Map.of("error", exception.getMessage());
   }

   @ExceptionHandler(UserIsNotOwnerOfBookedItem.class)
   @ResponseStatus(HttpStatus.NOT_FOUND)
   public Map<String, String> handleUserIsNotOwnerOfBookedItem(
           final UserIsNotOwnerOfBookedItem exception) {
      log.debug("Получен статус {}", HttpStatus.NOT_FOUND, exception);
      return Map.of("error", exception.getMessage());
   }

   @ExceptionHandler(BookingSetStatusByOwnerException.class)
   @ResponseStatus(HttpStatus.BAD_REQUEST)
   public Map<String, String> handleBookingSetStatusByOwnerException(
           final BookingSetStatusByOwnerException exception) {
      log.debug("Получен статус {}", HttpStatus.BAD_REQUEST, exception);
      return Map.of("error", exception.getMessage());
   }

   @ExceptionHandler(CreateBookingByOwnerException.class)
   @ResponseStatus(HttpStatus.NOT_FOUND)
   public Map<String, String> handleCreateBookingByOwnerException(
           final CreateBookingByOwnerException exception) {
      log.debug("Получен статус {}", HttpStatus.NOT_FOUND, exception);
      return Map.of("error", exception.getMessage());
   }

   @ExceptionHandler(NoBookingsToItemException.class)
   @ResponseStatus(HttpStatus.BAD_REQUEST)
   public Map<String, String> handleNoBookingsToItemException(
           final NoBookingsToItemException exception) {
      log.debug("Получен статус {}", HttpStatus.BAD_REQUEST, exception);
      return Map.of("error", exception.getMessage());
   }

   @ExceptionHandler(CreateNewUserException.class)
   @ResponseStatus(HttpStatus.BAD_REQUEST)
   public Map<String, String> handleCreateNewUserException(
           final CreateNewUserException exception) {
      log.debug("Получен статус {}", HttpStatus.BAD_REQUEST, exception);
      return Map.of("error", exception.getMessage());
   }

   @ExceptionHandler(UpdateUserException.class)
   @ResponseStatus(HttpStatus.BAD_REQUEST)
   public Map<String, String> handleUpdateUserException(
           final UpdateUserException exception) {
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
