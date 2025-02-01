package ru.practicum.exception;

public class DateBadRequestException extends RuntimeException {
    public DateBadRequestException(String message) {
        super(message);
    }
}
