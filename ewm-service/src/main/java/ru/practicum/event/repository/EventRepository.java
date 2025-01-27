package ru.practicum.event.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.event.model.Event;
import ru.practicum.event.model.EventState;

import java.time.LocalDateTime;
import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {

    List<Event> findAllByInitiatorId(Long initiatorId, Pageable pageable);

    Event findByInitiatorIdAndId(Long initiatorId, Long eventId);

    List<Event> findByIdInAndStateAndIdInAndEventDateBetween(
            List<Long> user,
            EventState state,
            List<Long> category,
            LocalDateTime startDate,
            LocalDateTime endDate,
            Pageable pageable
    );

}
