package ru.practicum.shareit.request.repository;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.request.entity.ItemRequestEntity;
import ru.practicum.shareit.user.entity.UserEntity;

import java.util.Collection;

public interface ItemRequestRepository extends JpaRepository<ItemRequestEntity, Integer> {

  Collection<ItemRequestEntity> findByAuthorOrderByCreated(UserEntity author);

  @Transactional(readOnly = true)
  Collection<ItemRequestEntity> findByAuthorNot(UserEntity user, PageRequest page);

}
