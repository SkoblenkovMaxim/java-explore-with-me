package ru.practicum.event.service;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.StatsClient;
import ru.practicum.category.mapper.CategoryMapper;
import ru.practicum.category.model.Category;
import ru.practicum.category.repository.CategoryRepository;
import ru.practicum.dto.EndpointHitDto;
import ru.practicum.event.dto.*;
import ru.practicum.event.mapper.EventMapper;
import ru.practicum.event.model.Event;
import ru.practicum.event.model.EventState;
import ru.practicum.event.repository.EventRepository;
import ru.practicum.exception.IllegalArgumentException;
import ru.practicum.exception.IncorrectDataException;
import ru.practicum.exception.NotFoundException;
import ru.practicum.user.model.User;
import ru.practicum.user.repository.UserRepository;
import ru.practicum.user.service.UserServiceImpl;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
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
    private final CategoryMapper categoryMapper;

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
                        LocalDateTime.now().minusDays(365),
                        LocalDateTime.now(), false, statsClient)))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public EventFullDto getEventByIdPrivate(Long userId, Long eventId) {

        log.info("Получен запрос на получение полной информации о событии пользователя");
        Event event = eventRepository.findByInitiatorIdAndId(eventId, userId);

        Long view = getHitsEvent(
                event.getId(),
                LocalDateTime.now().minusDays(100),
                LocalDateTime.now(), false, statsClient
        );

        return eventMapper.toFull(event, view);
    }

    @Override
    @Transactional
    public EventFullDto updateEventPrivate(Long userId, Long eventId, UpdateEventUserRequest updateEventUserRequest) {

        Event event = eventRepository.findById(eventId).orElseThrow(
                () -> new NotFoundException("Событие не найдено или недоступно")
        );

        User user = userRepository.findById(userId).orElseThrow(
                () -> new NotFoundException("Пользователь не найден")
        );

        if (event.getState().equals(EventState.PUBLISHED)) {
            throw new IllegalArgumentException("Должно быть PENDING или CANCELED");
        }

        log.info("Получен запрос на изменение события пользователя");
        if (updateEventUserRequest.getAnnotation() != null) {
            event.setAnnotation(updateEventUserRequest.getAnnotation());
        }

        if (updateEventUserRequest.getCategory() != null) {
            event.setCategory(categoryRepository.getById(updateEventUserRequest.getCategory()));
        }

        if (updateEventUserRequest.getDescription() != null) {
            event.setDescription(updateEventUserRequest.getDescription());
        }

        if (updateEventUserRequest.getEventDate() != null) {
            if (updateEventUserRequest.getEventDate().isBefore(LocalDateTime.now().plusHours(2))) {
                throw new IncorrectDataException("Начало события должно быть не менее 2 часов от его создания");
            } else {
                event.setEventDate(updateEventUserRequest.getEventDate());
            }
        }

        if (updateEventUserRequest.getLocation() != null) {
            if (updateEventUserRequest.getLocation().getLat() != null) {
                event.setLat(updateEventUserRequest.getLocation().getLat());
            }
            if (updateEventUserRequest.getLocation().getLon() != null) {
                event.setLon(updateEventUserRequest.getLocation().getLon());
            }
        }

        if (updateEventUserRequest.getPaid() != null) {
            event.setPaid(updateEventUserRequest.getPaid());
        }

        if (updateEventUserRequest.getParticipantLimit() != null) {
            event.setParticipantLimit(updateEventUserRequest.getParticipantLimit());
        }

        if (updateEventUserRequest.getRequestModeration() != null) {
            event.setRequestModeration(updateEventUserRequest.getRequestModeration());
        }

        if (updateEventUserRequest.getStateAction() != null) {
            if (updateEventUserRequest.getStateAction().equals("SEND_TO_REVIEW")) {
                event.setState(EventState.PENDING);
            } else if (updateEventUserRequest.getStateAction().equals("CANCEL_REVIEW")) {
                event.setState(EventState.CANCELED);
            } else {
                throw new IllegalArgumentException("Событие должно иметь статус PENDING при создании и статус CANCELED после выполнения запроса");
            }
        }

        if (updateEventUserRequest.getTitle() != null) {
            event.setTitle(updateEventUserRequest.getTitle());
        }

        log.info("Событие изменено");
        return eventMapper.toFull(eventRepository.save(event), 0L);

    }

    @Override
    @Transactional
    public List<EventFullDto> getEventsByIdsAdmin(
            List<Long> users,
            EventState states,
            List<Long> categories,
            LocalDateTime rangeStart,
            LocalDateTime rangeEnd,
            Pageable pageable
    ) {
        log.info("Получен запрос на получение событий по критериям");
        List<Event> eventList = eventRepository.findByIdInAndStateAndIdInAndEventDateBetween(
                users,
                states,
                categories,
                rangeStart,
                rangeEnd,
                pageable
        );

        List<EventFullDto> eventFullDtoList = new ArrayList<>();

        for (Event event : eventList) {
            Long view = getHitsEvent(
                    event.getId(),
                    LocalDateTime.of(1975, 1, 1, 1, 0),
                    LocalDateTime.now(), false, statsClient
            );
            eventFullDtoList.add(eventMapper.toFull(event, view));
        }

        return eventFullDtoList;
    }

    @Override
    //@Transactional
    public EventFullDto updateEventAdmin(Long eventId, UpdateEventAdminRequest updateEventAdminRequest) {

        log.info("Получен запрос на обновление события админом");
        Event event = eventRepository.findById(eventId).orElseThrow(
                () -> new NotFoundException("Событие не найдено или недоступно")
        );

        if (updateEventAdminRequest.getStateAction() != null) {
            if (updateEventAdminRequest.getStateAction().equals("PUBLISH_EVENT")) {
                if (!String.valueOf(event.getState()).equals("PENDING")) {
                    throw new IllegalArgumentException("Состояние события должно быть PENDING");
                }
                event.setState(EventState.PUBLISHED);
                event.setPublishedOn(LocalDateTime.now());
            } else if (updateEventAdminRequest.getStateAction().equals("REJECT_EVENT")) {
                if (String.valueOf(event.getState()).equals("PUBLISHED")) {
                    throw new IllegalArgumentException("Событие не может быть REJECT");
                }
                event.setState(EventState.CANCELED);
            } else {
                throw new IllegalArgumentException("StateAction должно быть PUBLISH_EVENT или REJECT_EVENT");
            }
        }

        if (updateEventAdminRequest.getAnnotation() != null) {
            event.setAnnotation(updateEventAdminRequest.getAnnotation());
        }

        if (updateEventAdminRequest.getCategory() != null && updateEventAdminRequest.getCategory().getId() != 0) {
            Category category = categoryRepository.getById(updateEventAdminRequest.getCategory().getId());
            event.setCategory(category);
        }

        if (updateEventAdminRequest.getDescription() != null) {
            event.setDescription(updateEventAdminRequest.getDescription());
        }

        if (updateEventAdminRequest.getEventDate() != null) {
            if (updateEventAdminRequest.getEventDate().isBefore(LocalDateTime.now().minusHours(1))) {
                throw new IncorrectDataException("Дата начала изменяемого события должна быть не ранее чем за час от даты публикации");
            }
            event.setEventDate(updateEventAdminRequest.getEventDate());
        }

        if (updateEventAdminRequest.getLocation() != null) {
            event.setLon(updateEventAdminRequest.getLocation().getLon());
            event.setLat(updateEventAdminRequest.getLocation().getLat());
        }

        if (updateEventAdminRequest.getParticipantLimit() != null) {
            event.setParticipantLimit(updateEventAdminRequest.getParticipantLimit());
        }

        if (updateEventAdminRequest.getPaid() != null) {
            event.setPaid(updateEventAdminRequest.getPaid());
        }

        if (updateEventAdminRequest.getRequestModeration() != null) {
            event.setRequestModeration(updateEventAdminRequest.getRequestModeration());
        }

        if (updateEventAdminRequest.getTitle() != null) {
            event.setTitle(updateEventAdminRequest.getTitle());
        }

        event.setId(eventId);
        eventRepository.save(event);
        return eventMapper.toFull(event, getHitsEvent(eventId,
                LocalDateTime.now().minusDays(100),
                LocalDateTime.now(),
                false,
                statsClient));
    }

    @Override
    @Transactional
    public List<EventShortDto> getEventsPublic(
            String text,
            List<Long> categories,
            Boolean paid,
            LocalDateTime rangeStart,
            LocalDateTime rangeEnd,
            Boolean onlyAvailable,
            String sort,
            Pageable pageable,
            HttpServletRequest request
    ) {

        EndpointHitDto endpointHitDto = new EndpointHitDto();
        endpointHitDto.setIp(request.getRemoteAddr());
        endpointHitDto.setUri(request.getRequestURI());
        endpointHitDto.setApp("ewm-main-service");
        endpointHitDto.setTimestamp(LocalDateTime.now());
        statsClient.hit(endpointHitDto);

        return null;

    }

}
