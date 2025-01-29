package ru.practicum.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.event.model.Location;

import java.time.LocalDateTime;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NewEventDto {

    @NotNull
    @NotBlank
    @Size(min = 3, max = 120)
    private String title; // Заголовок

    @NotBlank
    @NotNull
    @Size(min = 20, max = 2000)
    private String annotation; // Краткое описание

    @NotNull
    private Long category;

    private boolean paid; // Нужно ли оплачивать участие

    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate; // Дата и время, на которые намечено событие (в формате "yyyy-MM-dd HH:mm:ss")

    @NotNull
    @NotBlank
    @Size(min = 20, max = 7000)
    private String description; // Полное описание события

    @PositiveOrZero
    private Long participantLimit; // Ограничение на количество участников. Значение 0 - означает отсутствие ограничения

    @NotNull
    private Location location; // Широта и долгота места проведения события


    private boolean requestModeration; // Нужна ли пре-модерация заявок на участие

}
