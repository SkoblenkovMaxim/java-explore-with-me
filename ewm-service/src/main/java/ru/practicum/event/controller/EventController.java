package ru.practicum.event.controller;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import ru.practicum.event.dto.*;
import ru.practicum.event.model.EventState;
import ru.practicum.event.service.EventService;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RestController
//@RequestMapping(path = "/users/{userId}/events")
@RequiredArgsConstructor
public class EventController {

    private final EventService eventService;

    @PostMapping ("/users/{userId}/events")
    @ResponseStatus(HttpStatus.CREATED)
    public EventFullDto addEventPrivate(@PathVariable Long userId,
                                        @RequestBody NewEventDto newEventDto
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
            @RequestBody UpdateEventUserRequest updateEventUserRequest) {
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

}
