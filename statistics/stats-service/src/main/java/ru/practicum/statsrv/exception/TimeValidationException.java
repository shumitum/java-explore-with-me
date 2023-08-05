package ru.practicum.statsrv.exception;

public class TimeValidationException extends RuntimeException {
    public TimeValidationException(String message) {
        super(message);
    }
}