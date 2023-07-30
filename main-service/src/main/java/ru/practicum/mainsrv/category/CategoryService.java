package ru.practicum.mainsrv.category;

import java.util.List;

public interface CategoryService {
    CategoryDto createUser(CategoryDto categoryDto);

    CategoryDto editCategoryName(CategoryDto categoryDto, Long catId);

    void deleteCategoryById(Long catId);

    List<CategoryDto> getCategories(int from, int size);

    CategoryDto getCategoryById(Long catId);
}