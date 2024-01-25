package ru.practicum.shareit.request.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.factory.Mappers;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dto.InputItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.entity.ItemRequestEntity;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.mapper.UserMapper;

import java.util.Collection;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, uses = {UserMapper.class, ItemMapper.class})
public interface ItemRequestMapper {

  ItemRequestMapper INSTANCE = Mappers.getMapper(ItemRequestMapper.class);

  ItemRequest toModel(InputItemRequestDto input);

  ItemRequest toModel(ItemRequestEntity entity);

  Collection<ItemRequest> toModel(Collection<ItemRequestEntity> entities);

  ItemRequestDto toItemRequestDto(ItemRequest model);

  Collection<ItemRequestDto> toItemRequestDto(Collection<ItemRequest> models);

  ItemRequestDto.Item toItemDto(Item model);

  Collection<ItemRequestDto.Item> toItemDto(Collection<Item> models);

  @Mapping(source = "description", target = "description")
  @Mapping(source = "created", target = "created")
  @Mapping(source = "id", target = "id")
  @Mapping(source = "author", target = "author")
  ItemRequestEntity toEntity(ItemRequest model);

}
