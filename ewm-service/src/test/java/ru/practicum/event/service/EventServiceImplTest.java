package ru.practicum.event.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.category.dto.CategoryDto;
import ru.practicum.category.model.Category;
import ru.practicum.category.repository.CategoryRepository;
import ru.practicum.event.dto.NewEventDto;
import ru.practicum.event.model.Location;
import ru.practicum.event.repository.EventRepository;
import ru.practicum.user.dto.NewUserRequest;
import ru.practicum.user.model.User;
import ru.practicum.user.repository.UserRepository;
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

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    private User user;
    private NewUserRequest newUserRequest;
    private NewEventDto newEventDto;
    private Category category;
    private CategoryDto categoryDto;

    @BeforeEach
    void setUp() {

        category = Category.builder().name("Category 1").build();

        categoryDto = CategoryDto.builder().name("Category 2").build();

        categoryRepository.save(category);

        user = User.builder()
                .id(1L)
                .name("TestName")
                .email("test@email.ru")
                .build();

        userRepository.save(user);

        newUserRequest = NewUserRequest.builder()
                .name("Test123")
                .email("Test123@mail.ru")
                .build();

        newEventDto = NewEventDto.builder()
                .annotation("annotation")
                .category(1L)
                .description("")
                .eventDate(LocalDateTime.now().plusDays(1))
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