package ru.practicum.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {

    private Long id; // id пользователя

    @Size(min = 2, max = 250)
    @NotNull
    @NotBlank
    private String name; // имя пользователя

    @Size(min = 6, max = 254)
    @Email
    @NotBlank
    @NotNull
    private String email; // email пользователя
}
