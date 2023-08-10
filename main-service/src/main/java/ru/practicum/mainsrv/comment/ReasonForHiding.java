package ru.practicum.mainsrv.comment;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum ReasonForHiding {
    RULES("Нарушение правил"),
    OBSCENE_LANGUAGE("Нецензурная лексика"),
    SPAM("Спам");

    private final String reason;
}