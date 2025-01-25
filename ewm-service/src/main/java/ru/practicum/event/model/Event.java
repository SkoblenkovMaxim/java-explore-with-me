package ru.practicum.event.model;

import jakarta.persistence.*;
import lombok.Data;
import ru.practicum.category.model.Category;
import ru.practicum.user.model.User;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "events")
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "event_id")
    private Long id; // Идентификатор

    @Column(name = "title")
    private String title; // Заголовок

    @Column(name = "annotation")
    private String annotation; // Краткое описание

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category; // Категория

    @Column(name = "paid")
    private Boolean paid; // Нужно ли оплачивать участие

    @Column(name = "event_date")
    private LocalDateTime eventDate; // Дата и время на которые намечено событие (в формате "yyyy-MM-dd HH:mm:ss")

    @ManyToOne
    @JoinColumn(name = "initiator_id")
    private User initiator; // Пользователь

    @Column(name = "description")
    private String description; // Полное описание события

    @Column(name = "participant_limit")
    private Integer participantLimit; // Ограничение на количество участников. Значение 0 - означает отсутствие ограничения

    @Enumerated(value = EnumType.STRING)
    private EventState state; // Список состояний жизненного цикла события

    @Column(name = "created_on")
    private LocalDateTime createdOn; // Дата и время создания события (в формате "yyyy-MM-dd HH:mm:ss")

    @Column(name = "lat")
    private Float lat; // Широта

    @Column(name = "lon")
    private Float lon; // Долгота

    @Column(name = "request_moderation")
    private Boolean requestModeration; // Нужна ли пре-модерация заявок на участие

    @Column(name = "confirmed_requests")
    private Long confirmedRequests; // Количество одобренных заявок на участие в данном событии

    @Column(name = "published_on")
    private LocalDateTime publishedOn; // Дата и время публикации события (в формате "yyyy-MM-dd HH:mm:ss")

}
