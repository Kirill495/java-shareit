package ru.practicum.shareit.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import ru.practicum.shareit.booking.validators.StartEndProperties;

import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
@StartEndProperties()
public class InputBookingRequest {

  @NotNull
  @Positive
  private Integer itemId;

  @NotNull
  @FutureOrPresent
  private LocalDateTime start;

  @NotNull
  @FutureOrPresent
  private LocalDateTime end;

}
