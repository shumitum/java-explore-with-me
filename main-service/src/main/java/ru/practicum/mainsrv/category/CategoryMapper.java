package ru.practicum.mainsrv.category;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    Category toCategory(CategoryDto categoryDto);

    CategoryDto toCategoryDto(Category category);
}