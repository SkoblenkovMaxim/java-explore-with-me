package ru.practicum.category.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.category.dto.CategoryDto;
import ru.practicum.category.mapper.CategoryMapper;
import ru.practicum.category.model.Category;
import ru.practicum.category.repository.CategoryRepository;
import ru.practicum.exception.NotFoundException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Override
    public CategoryDto addCategory(CategoryDto categoryDto) {
        Category toSaveCategory = categoryMapper.toCategory(categoryDto);
        Category savedCategory = categoryRepository.save(toSaveCategory);
        return categoryMapper.toCategoryDto(savedCategory);
    }

    @Override
    public List<CategoryDto> getCategories(Integer from, Integer size) {
        return categoryRepository
                .findAll(PageRequest.of(from, size))
                .getContent().stream()
                .map(categoryMapper::toCategoryDto).toList();
    }

    @Override
    public CategoryDto getCategory(Long catId) {
        return categoryMapper.toCategoryDto(
                categoryRepository.findById(catId)
                        .orElseThrow(() -> new NotFoundException("Category not found"))
        );
    }

    @Override
    public void deleteCategory(Long catId) {
        if (categoryRepository.existsById(catId)) categoryRepository.deleteById(catId);
    }

    @Override
    public CategoryDto updateCategory(Long catId, CategoryDto categoryDto) {
        categoryDto.setId(catId);
        Category savedCategory = categoryRepository.save(categoryMapper.toCategory(categoryDto));
        return categoryMapper.toCategoryDto(savedCategory);
    }

}
