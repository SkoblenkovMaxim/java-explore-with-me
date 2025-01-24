package ru.practicum.event.service;

import com.sun.jdi.request.EventRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.category.dto.CategoryDto;
import ru.practicum.category.dto.NewCategoryDto;
import ru.practicum.category.model.Category;
import ru.practicum.event.dto.NewEventDto;
import ru.practicum.event.model.Location;
import ru.practicum.event.repository.EventRepository;
import ru.practicum.user.model.User;
import ru.practicum.user.service.UserService;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class EventServiceImplTest {

    @Autowired
    private EventService eventService;

    @Autowired
    private UserService userService;

    @Autowired
    private EventRepository eventRepository;

    private User user;
    private NewEventDto newEventDto;
    private CategoryDto category;

    @BeforeEach
    void setUp() {

        category = CategoryDto.builder().name("Category 1").build();

        user = User.builder()
                .id(1L)
                .name("TestName")
                .email("test@email.ru")
                .build();

        newEventDto = NewEventDto.builder()
                .annotation("annotation")
                .category(category)
                .description("")
                .eventDate(LocalDateTime.now())
                .location(Location.builder().lat(0.0F).lon(1.1F).build())
                .paid(true)
                .participantLimit(5)
                .requestModeration(false)
                .title("TestTitle")
                .build();
    }

    @Test
    void addEventPrivate() {
        eventService.addEventPrivate(user.getId(), newEventDto);

        assertEquals(1, eventRepository.findAll().size());
    }
}