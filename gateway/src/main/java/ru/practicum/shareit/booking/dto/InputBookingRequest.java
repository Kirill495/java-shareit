package ru.practicum.shareit.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
//@StartEndProperties
public class InputBookingRequest {

  @NotNull
  @Positive
  private Integer itemId;

  @NotNull
  //@Future
  private LocalDateTime start;

  @NotNull
//  @Future
  private LocalDateTime end;

}
