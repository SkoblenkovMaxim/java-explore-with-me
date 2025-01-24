package ru.practicum.compilation.service;

import ru.practicum.compilation.dto.CompilationDto;

import java.util.List;

public interface CompilationService {

    CompilationDto addCompilation(CompilationDto request);

    List<CompilationDto> getCompilations(Integer from, Integer size);

    CompilationDto getCompilation(Long catId);

    void deleteCompilation(Long catId);

    CompilationDto updateCompilation(Long catId, CompilationDto compilationDto);

}
