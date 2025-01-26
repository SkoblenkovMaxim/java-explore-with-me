package ru.practicum.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;
import ru.practicum.category.dto.CategoryDto;
import ru.practicum.event.model.Location;

import java.time.LocalDateTime;

@Data
@Builder
public class UpdateEventAdminRequest {

    @Size(min = 20, max = 2000)
    private String annotation; // Краткое описание

    private CategoryDto category;

    @Size(min = 20, max = 7000)
    private String description; // Полное описание события

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate; // Дата и время на которые намечено событие (в формате "yyyy-MM-dd HH:mm:ss")

    private Location location; // Широта и долгота места проведения события

    private boolean paid; // Нужно ли оплачивать участие

    private Integer participantLimit; // Ограничение на количество участников. Значение 0 - означает отсутствие ограничения

    private boolean requestModeration; // Нужна ли пре-модерация заявок на участие

    private String stateAction; // Новое состояние события - PUBLISH_EVENT, REJECT_EVENT

    @Size(min = 3, max = 120)
    private String title; // Новый заголовок

}
