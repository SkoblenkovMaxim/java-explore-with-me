package ru.practicum.event.service;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Pageable;
import ru.practicum.event.dto.*;
import ru.practicum.event.model.Event;
import ru.practicum.event.model.EventState;

import java.time.LocalDateTime;
import java.util.List;

public interface EventService {

    EventFullDto addEventPrivate(Long userId, NewEventDto newEventDto);

    List<EventShortDto> getEventsByUserIdPrivate(Long userId, Pageable pageable);

    EventFullDto getEventByIdPrivate(Long userId, Long eventId);

    EventFullDto updateEventPrivate(Long userId, Long eventId, UpdateEventUserRequest updateEventUserRequest);

    List<EventFullDto> getEventsByIdsAdmin(
            List<Long> users,
            EventState states,
            List<Long> categories,
            LocalDateTime rangeStart,
            LocalDateTime rangeEnd,
            Pageable pageable
    );

    EventFullDto updateEventAdmin(Long eventId, UpdateEventAdminRequest updateEventAdminRequest);

    List<EventShortDto> getEventsPublic(
            String text,
            List<Long> categories,
            Boolean paid,
            LocalDateTime rangeStart,
            LocalDateTime rangeEnd,
            Boolean onlyAvailable,
            String sort,
            Pageable pageable,
            HttpServletRequest request
    );

    EventFullDto getEventByIdPublic(Long eventId, HttpServletRequest request);

}
