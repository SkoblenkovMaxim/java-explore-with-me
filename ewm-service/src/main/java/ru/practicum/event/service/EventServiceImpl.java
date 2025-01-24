package ru.practicum.event.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.StatsClient;
import ru.practicum.category.model.Category;
import ru.practicum.category.repository.CategoryRepository;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.EventShortDto;
import ru.practicum.event.dto.NewEventDto;
import ru.practicum.event.mapper.EventMapper;
import ru.practicum.event.model.Event;
import ru.practicum.event.repository.EventRepository;
import ru.practicum.exception.IncorrectDataException;
import ru.practicum.exception.NotFoundException;
import ru.practicum.user.model.User;
import ru.practicum.user.repository.UserRepository;
import ru.practicum.utils.HitsEventViewUtil;

import java.time.LocalDateTime;

@Slf4j
@RequiredArgsConstructor
@Service
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;
    private final EventMapper eventMapper;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final StatsClient statsClient;
    private HitsEventViewUtil hitsEventViewUtil;

    @Override
    public EventShortDto getEventsByUsers(Long userId, int from, int size) {
        return null;
    }

    @Override
    public EventFullDto addEventPrivate(Long userId, NewEventDto newEventDto) {

        if (newEventDto.getEventDate().isBefore(LocalDateTime.now().plusHours(2))) {
            throw new IncorrectDataException("Событие не удовлетворяет правилам создания");
        }

        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("Пользователь не найден"));

        Category category = categoryRepository.getById(newEventDto.getCategory().getId());

        Event event = eventMapper.toEvent(newEventDto, category, user, null);
        event = eventRepository.save(event);

        log.info("Событие добавлено");
        return eventMapper.toFull(event, hitsEventViewUtil.getHitsEvent(
                event.getId(),
                LocalDateTime.now().minusDays(365),
                LocalDateTime.now(),
                false,
                statsClient)
        );
    }
}
