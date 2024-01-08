package ru.practicum.shareit.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.user.validation.CreateNewUserInfo;
import ru.practicum.shareit.user.validation.UpdateUserInfo;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(setterPrefix = "with")
public class UserDto {
   private Integer id;
   @NotNull(groups = CreateNewUserInfo.class)
   private String name;
   @Email(groups = {UpdateUserInfo.class, CreateNewUserInfo.class})
   @NotNull(groups = CreateNewUserInfo.class)
   private String email;
}
