package ru.practicum.categories.service;

import ru.practicum.categories.dto.CategoryDto;
import ru.practicum.categories.dto.NewCategoryDto;

public interface CategoryService {

    CategoryDto addCategory(NewCategoryDto request);

}
