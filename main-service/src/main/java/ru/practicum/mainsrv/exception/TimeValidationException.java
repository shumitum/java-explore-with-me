package ru.practicum.mainsrv.exception;

public class TimeValidationException extends RuntimeException {
    public TimeValidationException(String message) {
        super(message);
    }
}