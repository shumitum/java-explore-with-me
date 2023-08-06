package ru.practicum.mainsrv.event.enums;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;

@RequiredArgsConstructor
public enum SortBy {
    EVENT_DATE("eventDate"),
    VIEWS("views");

    private final String sort;

    public Sort descending() {
        return Sort.by(Sort.Direction.DESC, this.sort);
    }
}