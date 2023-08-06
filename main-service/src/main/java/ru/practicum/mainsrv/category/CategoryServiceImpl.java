package ru.practicum.mainsrv.category;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.mainsrv.event.EventRepository;
import ru.practicum.mainsrv.exception.ConflictException;
import ru.practicum.mainsrv.utility.PageParam;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository catRepository;
    private final EventRepository eventRepository;
    private final CategoryMapper catMapper;

    @Override
    @Transactional
    public CategoryDto createUser(CategoryDto categoryDto) {
        CategoryDto newCatDto = catMapper.toCategoryDto(catRepository.save(catMapper.toCategory(categoryDto)));
        log.info("Создана новая категория: {}", newCatDto);
        return newCatDto;
    }

    @Override
    @Transactional
    public CategoryDto editCategoryName(CategoryDto categoryDto, Long catId) {
        final Category updatingCategory = findCategoryById(catId);
        if (categoryDto.getName().equals(updatingCategory.getName())) {
            return catMapper.toCategoryDto(updatingCategory);
        }
        updatingCategory.setName(categoryDto.getName());
        log.info("Категория с ID:{} изменена. {}", catId, updatingCategory);
        return catMapper.toCategoryDto(updatingCategory);
    }

    @Override
    @Transactional
    public void deleteCategoryById(Long catId) {
        checkCategoryExistence(catId);
        if (eventRepository.existsEventByCategory_Id(catId)) {
            throw new ConflictException("с категорией не должно быть связано ни одного события.");
        }
        catRepository.deleteById(catId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CategoryDto> getCategories(int from, int size) {
        return catRepository.findAll(PageParam.of(from, size)).stream()
                .map(catMapper::toCategoryDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public CategoryDto getCategoryById(Long catId) {
        return catMapper.toCategoryDto(findCategoryById(catId));
    }

    @Override
    @Transactional(readOnly = true)
    public Category findCategoryById(long catId) {
        return catRepository.findById(catId)
                .orElseThrow(() -> new NoSuchElementException(String.format("Категории с ID=%d не существует", catId)));
    }

    @Transactional(readOnly = true)
    public void checkCategoryExistence(long catId) {
        if (!catRepository.existsById(catId)) {
            throw new NoSuchElementException(String.format("Категории с ID=%d не существует", catId));
        }
    }
}