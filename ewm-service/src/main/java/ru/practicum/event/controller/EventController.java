package ru.practicum.event.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.event.dto.*;
import ru.practicum.event.model.EventState;
import ru.practicum.event.service.EventService;

import java.time.LocalDateTime;
import java.util.List;

@Validated
@Slf4j
@RestController
@RequiredArgsConstructor
public class EventController {

    private final EventService eventService;

    @PostMapping ("/users/{userId}/events")
    @ResponseStatus(HttpStatus.CREATED)
    public EventFullDto addEventPrivate(@PathVariable Long userId,
                                        @Valid @RequestBody NewEventDto newEventDto
    ) {
        log.info("Получен запрос на добавление нового события {}", newEventDto);
        return eventService.addEventPrivate(userId, newEventDto);
    }

    @GetMapping("/users/{userId}/events")
    @ResponseStatus(HttpStatus.OK)
    public List<EventShortDto> getEventsByUserIdPrivate(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "0") Integer from,
            @RequestParam(defaultValue = "10") Integer size) {
        log.info("Получен запрос на получение событий пользователя");
        return eventService.getEventsByUserIdPrivate(userId, PageRequest.of(from, size));
    }

    @GetMapping("/users/{userId}/events/{eventId}")
    @ResponseStatus(HttpStatus.OK)
    public EventFullDto getEventById(
            @PathVariable Long userId,
            @PathVariable Long eventId) {
        log.info("Получение полной информации о событии пользователя");
        return eventService.getEventByIdPrivate(userId, eventId);
    }

    @PatchMapping("/users/{userId}/events/{eventId}")
    @ResponseStatus(HttpStatus.OK)
    public EventFullDto updateEventPrivate(
            @PathVariable Long userId,
            @PathVariable Long eventId,
            @Valid @RequestBody UpdateEventUserRequest updateEventUserRequest) {
        log.info("Получен запрос на изменение события");
        return eventService.updateEventPrivate(userId, eventId, updateEventUserRequest);
    }

    @GetMapping("/admin/events")
    @ResponseStatus(HttpStatus.OK)
    public List<EventFullDto> getEventsByIdsAdmin(
            @RequestParam(required = false) List<Long> users,
            @RequestParam(required = false) EventState states,
            @RequestParam(required = false) List<Long> categories,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeStart,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeEnd,
            @RequestParam(required = false, defaultValue = "0") Integer from,
            @RequestParam(required = false, defaultValue = "10") Integer size
    ) {
        log.info("Получение списка с полным описанием событий, найденных по критериям");
        return eventService.getEventsByIdsAdmin(
                users, states, categories, rangeStart, rangeEnd, PageRequest.of(from, size)
        );
    }

    @PatchMapping("/admin/events/{eventId}")
    @ResponseStatus(HttpStatus.OK)
    public EventFullDto updateEventAdmin(
            @PathVariable Long eventId,
            @RequestBody UpdateEventAdminRequest updateEventAdminRequest) {
        log.info("Запрос на изменение события админом");
        return eventService.updateEventAdmin(eventId, updateEventAdminRequest);
    }

    @GetMapping("/events")
    @ResponseStatus(HttpStatus.OK)
    public List<EventShortDto> getEventsPublic(
            @RequestParam(required = false) String text,
            @RequestParam(required = false) List<Long> categories,
            @RequestParam(required = false) Boolean paid,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeStart,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeEnd,
            @RequestParam(required = false) Boolean onlyAvailable,
            @RequestParam(required = false) String sort,
            @RequestParam(required = false, defaultValue = "0") Integer from,
            @RequestParam(required = false, defaultValue = "10") Integer size,
            HttpServletRequest request
    ) {
        log.info("Получение события по критериям");
        return eventService.getEventsPublic(
                text,
                categories,
                paid,
                rangeStart,
                rangeEnd,
                onlyAvailable,
                sort,
                PageRequest.of(from, size),
                request
        );
    }

    @GetMapping("/events/{id}")
    @ResponseStatus(HttpStatus.OK)
    public EventFullDto getEventByIdPublic(
            @PathVariable Long id,
            HttpServletRequest request
    ) {
        log.info("Получение события по его идентификатору");
        return eventService.getEventByIdPublic(id, request);
    }

}
