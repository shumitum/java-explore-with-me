package ru.practicum.statsrv.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.NoSuchElementException;

@Slf4j
@RestControllerAdvice
public class ErrorHandler {
    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNoSuchElementException(final NoSuchElementException e) {
        log.warn("Получен статус 404 NOT FOUND {}", e.getMessage());
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorResponse handleIllegalArgumentException(final IllegalArgumentException e) {
        log.warn("Получен статус 403 FORBIDDEN {}", e.getMessage());
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleTimeValidationException(final TimeValidationException e) {
        log.warn("Получен статус 400 BAD REQUEST {}", e.getMessage());
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler({DataIntegrityViolationException.class})
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleDataIntegrityViolationException(final Exception e) {
        log.warn("Получен статус 409 CONFLICT {}", e.getMessage());
        return new ErrorResponse(e.getMessage());
    }
}