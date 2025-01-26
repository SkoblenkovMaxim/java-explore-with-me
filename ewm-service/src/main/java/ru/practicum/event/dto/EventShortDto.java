package ru.practicum.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import ru.practicum.category.dto.CategoryDto;
import ru.practicum.user.dto.UserShortDto;

import java.time.LocalDateTime;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventShortDto {

    @Size(min = 20, max = 2000)
    private String annotation; // Краткое описание

    private CategoryDto category;

    private Long confirmedRequests; // Количество одобренных заявок на участие в данном событии

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate; // Дата и время на которые намечено событие (в формате "yyyy-MM-dd HH:mm:ss")

    private Long id; // Идентификатор

    private UserShortDto initiator; // Инициатор события

    private boolean paid; // Нужно ли оплачивать участие

    @Size(min = 3, max = 120)
    private String title; // Заголовок

    private Long views; // Количество просмотрев события
}
