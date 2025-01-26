package ru.practicum.event.service;

import org.springframework.data.domain.Pageable;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.EventShortDto;
import ru.practicum.event.dto.NewEventDto;

import java.util.List;

public interface EventService {

    List<EventShortDto> getEventsByUserIdPrivate(Long userId, Pageable pageable);

    EventFullDto addEventPrivate(Long userId, NewEventDto newEventDto);

}
