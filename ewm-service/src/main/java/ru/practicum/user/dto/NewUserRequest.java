package ru.practicum.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class NewUserRequest {

    @NotNull
    @NotBlank
    private String name; // имя пользователя

    @Email
    @NotBlank
    @NotNull
    private String email; // email пользователя

}
