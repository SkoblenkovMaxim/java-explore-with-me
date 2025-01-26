package ru.practicum.event.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
import ru.practicum.user.service.UserServiceImpl;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.utils.HitsEventViewUtil.getHitsEvent;

@Slf4j
@RequiredArgsConstructor
@Service
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;
    private final EventMapper eventMapper;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final UserServiceImpl userServiceImpl;
    private final StatsClient statsClient;

    @Override
    public EventFullDto addEventPrivate(Long userId, NewEventDto newEventDto) {

        if (newEventDto.getEventDate() != null
                && newEventDto.getEventDate().isBefore(LocalDateTime.now().plusHours(2))
        ) {
            throw new IncorrectDataException("Событие не удовлетворяет правилам создания");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден"));

        Category category = categoryRepository.findById(newEventDto.getCategory()).orElseThrow();

        Event event = eventMapper.toEvent(newEventDto, category, user);

        Event newEvent = eventRepository.save(event);

        log.info("Событие добавлено");
        return eventMapper.toFull(newEvent, 0L);
    }

    @Override
    @Transactional
    public List<EventShortDto> getEventsByUserIdPrivate(Long userId, Pageable pageable) {

        log.info("Получение событий пользователя");
        return eventRepository.findAllByInitiatorId(userId, pageable).stream()
                .map(e -> eventMapper.toShort(e, getHitsEvent(
                        e.getId(),
                        LocalDateTime.now().minusDays(100),
                        LocalDateTime.now(), false, statsClient)))
                .collect(Collectors.toList());
    }

    

}
