package ru.practicum.user.dto;

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
public class UserShortDto {

    private Long id; // id пользователя

    @Size(min = 2, max = 250)
    @NotBlank
    private String name; // фамилия и имя пользователя
}
