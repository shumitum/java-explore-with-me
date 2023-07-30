package ru.practicum.mainsrv.category;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CategoryDto {
    Long id;

    @Size(max = 50)
    @NotBlank//(message = "Поле Имя не должно быть пустым")
    String name;
}