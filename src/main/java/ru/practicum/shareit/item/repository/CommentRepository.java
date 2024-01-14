package ru.practicum.shareit.item.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.item.entity.CommentEntity;
import ru.practicum.shareit.item.entity.ItemEntity;

import java.util.List;

public interface CommentRepository extends JpaRepository<CommentEntity, Integer> {
  List<CommentEntity> findByItemOrderById(ItemEntity item);
}
