package ru.practicum.compilation.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.StatsClient;
import ru.practicum.compilation.dto.*;
import ru.practicum.compilation.mapper.CompilationMapper;
import ru.practicum.compilation.model.Compilation;
import ru.practicum.compilation.repository.CompilationRepository;
import ru.practicum.event.model.Event;
import ru.practicum.event.repository.EventRepository;
import ru.practicum.exception.NotFoundException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.utils.HitsEventViewUtil.getHitsEvent;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CompilationServiceImpl implements CompilationService {

    private final EventRepository eventRepository;
    private final CompilationRepository compilationRepository;
    private final CompilationMapper compilationMapper;
    private final StatsClient client;

    @Override
    @Transactional
    public CompilationDto addCompilationAdmin(NewCompilationDto newCompilationDto) {
        Compilation compilation = new Compilation();
        compilation.setTitle(newCompilationDto.getTitle());

        if (newCompilationDto.getEvents() != null) {
            List<Event> events = eventRepository.findAllByIdIn(newCompilationDto.getEvents());
            compilation.setEvents(events);
        } else {
            compilation.setEvents(new ArrayList<>());
        }

        if (newCompilationDto.getPinned() == null) {
            compilation.setPinned(false);
        } else {
            compilation.setPinned(newCompilationDto.getPinned());
        }

        compilationRepository.save(compilation);
        return compilationMapper.toDto(compilation, 0L);
    }

    @Override
    @Transactional
    public void deleteCompilationAdmin(Long compilationId) {
        if (compilationRepository.existsById(compilationId)) {
            compilationRepository.deleteById(compilationId);
            log.info(String.format("Compilation with id=%d not found", compilationId));
        }
    }

    @Override
    @Transactional
    public CompilationDto updateCompilationAdmin(Long compilationId, UpdateCompilationRequest updateCompilationRequest) {
        Compilation compilation = getCompilationOrThrow(compilationId);

        if (updateCompilationRequest.getEvents() != null && !updateCompilationRequest.getEvents().isEmpty()) {
            List<Event> events = eventRepository.findAllByIdIn(updateCompilationRequest.getEvents());
            compilation.setEvents(events);
        }
        if (updateCompilationRequest.getTitle() != null) {
            compilation.setTitle(updateCompilationRequest.getTitle());

        }
        if (updateCompilationRequest.getPinned() != null) {
            compilation.setPinned(updateCompilationRequest.getPinned());
        }
        compilationRepository.save(compilation);
        Long view = getHitsEvent(compilationId, LocalDateTime.now().minusDays(1000),
                LocalDateTime.now(), true, client);
        return compilationMapper.toDto(compilation, view);
    }

    @Override
    public List<CompilationDto> getCompilationsPublic(Boolean pinned, Pageable pageable) {
        if (pinned == null) {
            return compilationRepository.findAll(pageable).stream()
                    .map(c -> compilationMapper.toDto(c, getHitsEvent(c.getId(),
                            LocalDateTime.now().minusDays(100),
                            LocalDateTime.now(), true, client)))
                    .collect(Collectors.toList());
        }

        return compilationRepository.findAllByPinned(pinned, pageable).stream()
                .map(c -> compilationMapper.toDto(c, getHitsEvent(c.getId(),
                        LocalDateTime.now().minusDays(100),
                        LocalDateTime.now(), true, client)))
                .collect(Collectors.toList());
    }

    @Override
    public CompilationDto getCompilationPublic(Long compilationId) {
        Compilation compilation = getCompilationOrThrow(compilationId);
        return compilationMapper.toDto(compilation, getHitsEvent(compilationId,
                LocalDateTime.now().minusDays(100),
                LocalDateTime.now(), true, client));
    }

    private Compilation getCompilationOrThrow(Long compilationId) {
        return compilationRepository.findById(compilationId).orElseThrow(
                () -> new NotFoundException(String.format("Compilation with id=%d was not found", compilationId)));
    }

//    private final CompilationRepository compilationRepository;
//    private final CompilationMapper compilationMapper;
//    private final EventRepository eventRepository;
//
//    @Transactional
//    @Override
//    public CompilationDto addCompilationAdmin(NewCompilationDto newCompilationDto) {
//
//        Compilation compilationToSave = compilationMapper.toCompilation(newCompilationDto);
//        Compilation savedCompilation = compilationRepository.save(compilationToSave);
//        return compilationMapper.toCompilationDto(
//                savedCompilation,
//                getEventsByIds(savedCompilation.getEvents())
//        );
//    }
//
//    @Override
//    public void deleteCompilationAdmin(Long compId) {
//        compilationRepository.deleteById(compId);
//    }
//
//    @Override
//    public CompilationDto updateCompilationAdmin(Long compId, UpdateCompilationRequest updateCompilationRequest) {
//        Compilation compilationToUpdate = compilationRepository.findById(compId).orElseThrow();
//
//        if (updateCompilationRequest.getEvents() != null) {
//            List<Event> events = eventRepository.findAllByIdIn(updateCompilationRequest.getEvents());
//            List<Long> ids = new ArrayList<>();
//            for (Event event : events) {
//                ids.add(event.getId());
//            }
//            updateCompilationRequest.setEvents(ids);
//        }
//
//        if (updateCompilationRequest.getTitle() != null) {
//            compilationToUpdate.setTitle(updateCompilationRequest.getTitle());
//        }
//
//        if (updateCompilationRequest.getPinned() != null) {
//            compilationToUpdate.setPinned(updateCompilationRequest.getPinned());
//        }
//
////        compilationToUpdate.setTitle(updateCompilationRequest.getTitle());
////        compilationToUpdate.setPinned(updateCompilationRequest.getPinned());
//        //compilationToUpdate.setEvents(updateCompilationDto.getEvents());
//        Compilation updatedCompilation = compilationRepository.save(compilationToUpdate);
//        return compilationMapper.toCompilationDto(updatedCompilation, getEventsByIds(updatedCompilation .getEvents()));
//    }
//
//    @Override
//    public List<CompilationDto> getCompilations(Boolean pinned, int from, int size) {
//        List<Compilation> compilations = compilationRepository.findAll();
//
//        if (pinned == null) {
//            return compilations.stream().map(
//                    comp -> compilationMapper.toCompilationDto(comp, getEventsByIds(comp.getEvents()))
//            ).skip(from).limit(size).toList();
//        }
//
//        return compilations.stream().filter(
//                 comp -> comp.getPinned().equals(pinned)
//        ).map(
//                comp -> compilationMapper.toCompilationDto(comp, getEventsByIds(comp.getEvents()))
//        ).skip(from).limit(size).toList();
//    }
//
//    @Override
//    public CompilationDto getCompilation(Long compId) {
//        if (!compilationRepository.existsById(compId)) return new CompilationDto();
//        Compilation compilation = compilationRepository.findById(compId).orElseThrow();
//        return compilationMapper.toCompilationDto(compilation, getEventsByIds(compilation.getEvents()));
//    }
//
//    private List<Event> getEventsByIds(List<Long> events) {
//        return eventRepository.findAllByIdIn(events);
//    }

}
