package ru.practicum.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.category.dto.CategoryDto;
import ru.practicum.event.model.EventState;
import ru.practicum.event.model.Location;
import ru.practicum.user.dto.UserShortDto;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class EventFullDto {

    private Long id; // Идентификатор

    @Size(min = 3, max = 120)
    private String title; // Заголовок

    @Size(min = 20, max = 2000)
    private String annotation; // Краткое описание

    private CategoryDto category;

    private boolean paid; // Нужно ли оплачивать участие

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate; // Дата и время на которые намечено событие (в формате "yyyy-MM-dd HH:mm:ss")

    private UserShortDto initiator; // Инициатор события

    private Long views; // Количество просмотрев события

    private Long confirmedRequests; // Количество одобренных заявок на участие в данном событии

    @Size(min = 20, max = 7000)
    private String description; // Полное описание события

    private Long participantLimit; // Ограничение на количество участников. Значение 0 - означает отсутствие ограничения

    private EventState state; // Список состояний жизненного цикла события

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdOn; // Дата и время создания события (в формате "yyyy-MM-dd HH:mm:ss")

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime publishedOn; // Дата и время публикации события (в формате "yyyy-MM-dd HH:mm:ss")

    private Location location; // Широта и долгота места проведения события

    private boolean requestModeration; // Нужна ли пре-модерация заявок на участие

}
