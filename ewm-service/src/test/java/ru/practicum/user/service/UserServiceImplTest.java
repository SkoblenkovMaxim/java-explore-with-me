package ru.practicum.user.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.user.dto.NewUserRequest;
import ru.practicum.user.dto.UserDto;
import ru.practicum.user.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserServiceImplTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    private UserDto userDto;
    private NewUserRequest newUserRequest;

    @BeforeEach
    void setUp() {
        userDto = UserDto.builder()
                .id(1L)
                .name("Test")
                .email("test@test.com")
                .build();

        newUserRequest = NewUserRequest.builder()
                .name("Test1")
                .email("test1@test.com")
                .build();
    }

    @Test
    void addUserAdmin() {
        userService.addUserAdmin(newUserRequest);

        assertEquals(1, userRepository.findAll().size());
        assertTrue(userRepository.existsByEmail(newUserRequest.getEmail()));
    }

    @Test
    void getAllUsersAdmin() {
        userService.addUserAdmin(newUserRequest);
        List<Long> listUserDto = new ArrayList<>();
        listUserDto.add(userDto.getId());

        userService.getAllUsersAdmin(0, 10);

        assertEquals(1, userRepository.findAll().size());
    }

    @Test
    void deleteUserAdmin() {
        userService.addUserAdmin(newUserRequest);
        userService.deleteUserAdmin(1L);

        assertEquals(0, userRepository.findAll().size());
    }
}