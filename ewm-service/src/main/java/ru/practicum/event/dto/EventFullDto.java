package ru.practicum.event.dto;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
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

    @Size(min = 20, max = 2000)
    private String annotation; // Краткое описание

    private CategoryDto category;

    private Long confirmedRequests; // Количество одобренных заявок на участие в данном событии

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdOn; // Дата и время создания события (в формате "yyyy-MM-dd HH:mm:ss")

    @Size(min = 20, max = 7000)
    private String description; // Полное описание события

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate; // Дата и время на которые намечено событие (в формате "yyyy-MM-dd HH:mm:ss")

    private Long id; // Идентификатор

    private UserShortDto initiator; // Инициатор события

    private Location location; // Широта и долгота места проведения события

    private boolean paid; // Нужно ли оплачивать участие

    private Integer participantLimit; // Ограничение на количество участников. Значение 0 - означает отсутствие ограничения

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime publishedOn; // Дата и время публикации события (в формате "yyyy-MM-dd HH:mm:ss")

    private boolean requestModeration; // Нужна ли пре-модерация заявок на участие

    private EventState state; // Список состояний жизненного цикла события

    @Size(min = 3, max = 120)
    private String title; // Заголовок

    private Long views; // Количество просмотрев события

}
