package ru.practicum.shareit.booking.mapper;

import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;
import ru.practicum.shareit.booking.dto.BookingResponse;
import ru.practicum.shareit.booking.dto.InputBookingRequest;
import ru.practicum.shareit.booking.entity.BookingEntity;
import ru.practicum.shareit.booking.model.Booking;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        builder = @Builder(disableBuilder = true))
public interface BookingMapper {

  BookingMapper MAPPER = Mappers.getMapper(BookingMapper.class);

  @Mapping(target = "status", constant = "WAITING")
  Booking toModel(InputBookingRequest request);

  Booking toModel(BookingEntity entity);

  List<Booking> toModel(List<BookingEntity> entities);

  BookingEntity toEntity(Booking model);

  BookingResponse toResponse(Booking model);

  List<BookingResponse> toResponse(List<Booking> models);


}
