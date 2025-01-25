package ru.practicum.event.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import ru.practicum.event.model.Location;

import java.time.LocalDateTime;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NewEventDto {

    @NotNull
    private String title; // Заголовок

    @NotBlank
    @NotNull
    private String annotation; // Краткое описание

    @NotNull
    private Long categoryId;

    private boolean paid; // Нужно ли оплачивать участие

    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate; // Дата и время, на которые намечено событие (в формате "yyyy-MM-dd HH:mm:ss")

    private String description; // Полное описание события

    private Integer participantLimit; // Ограничение на количество участников. Значение 0 - означает отсутствие ограничения

    private Location location; // Широта и долгота места проведения события

    private boolean requestModeration; // Нужна ли пре-модерация заявок на участие

}
