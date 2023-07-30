package ru.practicum.mainsrv.user;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserDto {
    Long id;

    @Size(min = 2, max = 250)
    @NotBlank//(message = "Поле Имя не должно быть пустым")
    String name;


    @Email
    @Size(min = 6, max = 254)
    @NotBlank//(message = "Поле email не должно быть пустым")
    String email;
}