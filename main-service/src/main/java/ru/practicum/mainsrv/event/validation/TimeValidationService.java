package ru.practicum.mainsrv.event.validation;


import org.springframework.stereotype.Service;
import ru.practicum.mainsrv.exception.InvalidArgumentException;
import ru.practicum.mainsrv.exception.TimeValidationException;

import java.time.Duration;
import java.time.LocalDateTime;

@Service
public class TimeValidationService {
    public static final int MIN_HOURS_BEFORE_EVENT_FOR_ADMIN = 1;
    public static final int MIN_HOURS_BEFORE_EVENT_FOR_USER = 2;

    public void checkStartTimeIsBeforeEndTime(LocalDateTime rangeStart, LocalDateTime rangeEnd) {
        if (rangeStart != null && rangeEnd != null && (rangeEnd.isBefore(rangeStart))) {
            throw new InvalidArgumentException("Параметр дата и время начала выборки должен быть раньше даты и времени конца");
        }
    }

    public void validatePeriodBeforeEvent(LocalDateTime eventDate, int minHoursBeforeEvent) {
        if (eventDate != null && Duration.between(LocalDateTime.now(), eventDate).toHours() < minHoursBeforeEvent) {
            throw new TimeValidationException("Дата и время на которые намечено событие не может быть раньше, чем через два часа от текущего момента ");
        }
    }
}