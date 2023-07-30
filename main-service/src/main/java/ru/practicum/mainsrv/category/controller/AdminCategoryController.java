package ru.practicum.mainsrv.category.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.mainsrv.category.CategoryDto;
import ru.practicum.mainsrv.category.CategoryService;

import javax.validation.Valid;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/categories")
public class AdminCategoryController {
    private final CategoryService categoryService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryDto createCategory(@RequestBody @Valid CategoryDto categoryDto) {
        log.info("Запрос на создание категории: {}", categoryDto);
        return categoryService.createUser(categoryDto);
    }

    @PatchMapping("/{catId}")
    @ResponseStatus(HttpStatus.OK)
    public CategoryDto editCategoryName(@RequestBody @Valid CategoryDto categoryDto,
                                        @PathVariable Long catId) {
        log.info("Запрос изменение имени категории: {}", categoryDto);
        return categoryService.editCategoryName(categoryDto, catId);
    }


    @DeleteMapping("/{catId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCategoryById(@PathVariable Long catId) {
        categoryService.deleteCategoryById(catId);
        log.info("Категория с ID={} удалена", catId);
    }
}
