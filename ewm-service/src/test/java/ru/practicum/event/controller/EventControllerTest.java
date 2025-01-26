package ru.practicum.event.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.practicum.event.service.EventService;

import static org.junit.jupiter.api.Assertions.*;

@WebMvcTest(EventController.class)
@AutoConfigureMockMvc
class EventControllerTest {

    @MockBean
    private EventService eventService;

    @BeforeEach
    void setUp() {
    }

    @Test
    void addEventPrivate() {
    }
}