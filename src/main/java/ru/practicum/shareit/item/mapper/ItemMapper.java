package ru.practicum.shareit.item.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;
import ru.practicum.shareit.booking.dto.LightBookingDTO;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.dto.item.ItemResponse;
import ru.practicum.shareit.item.dto.item.NewItemRequest;
import ru.practicum.shareit.item.dto.item.UpdateItemRequest;
import ru.practicum.shareit.item.entity.ItemEntity;
import ru.practicum.shareit.item.model.Item;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        builder = @Builder(disableBuilder = true))
public interface ItemMapper {

  ItemMapper MAPPER = Mappers.getMapper(ItemMapper.class);

  Item toModel(ItemEntity entity);

  Item toModel(UpdateItemRequest request);

  Item toModel(NewItemRequest request);

  ItemEntity toEntity(Item model);

  ItemResponse toResponse(Item model);

  @BeanMapping(ignoreByDefault = true)
  @Mapping(target = "lastBooking.id", source = "id")
  @Mapping(target = "lastBooking.bookerId", source = "booker.id")
  void appendLastBooking(@MappingTarget ItemResponse response, Booking booking);

  @BeanMapping(ignoreByDefault = true)
  @Mapping(target = "lastBooking.id", source = "id")
  @Mapping(target = "lastBooking.bookerId", source = "bookerId")
  void appendLastBooking(@MappingTarget ItemResponse response, LightBookingDTO source);

  @BeanMapping(ignoreByDefault = true)
  @Mapping(target = "nextBooking.id", source = "id")
  @Mapping(target = "nextBooking.bookerId", source = "booker.id")
  void appendNextBooking(@MappingTarget ItemResponse response, Booking booking);

  @BeanMapping(ignoreByDefault = true)
  @Mapping(target = "nextBooking.id", source = "id")
  @Mapping(target = "nextBooking.bookerId", source = "bookerId")
  void appendNextBooking(@MappingTarget ItemResponse response, LightBookingDTO source);

}
