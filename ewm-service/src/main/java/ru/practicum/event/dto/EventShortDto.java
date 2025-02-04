package ru.practicum.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.category.dto.CategoryDto;
import ru.practicum.user.dto.UserShortDto;

import java.time.LocalDateTime;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventShortDto {

    private Long id; // Идентификатор

    @Size(min = 3, max = 120)
    private String title; // Заголовок

    @Size(min = 20, max = 2000)
    private String annotation; // Краткое описание

    private CategoryDto category;

    private Boolean paid; // Нужно ли оплачивать участие

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate; // Дата и время на которые намечено событие (в формате "yyyy-MM-dd HH:mm:ss")

    private UserShortDto initiator; // Инициатор события

    private Long views; // Количество просмотрев события

    private Long confirmedRequests; // Количество одобренных заявок на участие в данном событии

}
