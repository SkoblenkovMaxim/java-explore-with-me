package ru.practicum.user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.user.dto.NewUserRequest;
import ru.practicum.user.dto.UserDto;
import ru.practicum.user.service.UserServiceImpl;

@WebMvcTest(UserController.class)
@AutoConfigureMockMvc
class UserControllerTest {

    @MockBean
    private UserServiceImpl userServiceImpl;

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    private NewUserRequest newUserRequest;
    private UserDto userDto;

    @BeforeEach
    void setUp() {
        newUserRequest = NewUserRequest.builder()
                .email("test@email.com")
                .name("TestName")
                .build();

        userDto = UserDto.builder()
                .email("userDto@email.com")
                .id(1L)
                .name("userDto")
                .build();
    }

    @Test
    void addUserAdmin() {
    }

    @Test
    void getAllUsersAdmin() {
    }

    @Test
    void deleteUserAdmin() {
    }
}