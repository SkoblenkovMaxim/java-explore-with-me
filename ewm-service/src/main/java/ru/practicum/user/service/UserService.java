package ru.practicum.user.service;

import ru.practicum.user.dto.NewUserRequest;
import ru.practicum.user.dto.UserDto;

import java.util.List;

public interface UserService {

    UserDto addUserAdmin(NewUserRequest newUserRequest);

    List<UserDto> getAllUsersAdmin(int from, int size);

    void deleteUserAdmin(Long id);

}
