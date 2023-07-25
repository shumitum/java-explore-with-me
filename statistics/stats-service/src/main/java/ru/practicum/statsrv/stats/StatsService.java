package ru.practicum.statsrv.stats;

import ru.practicum.dto.EndpointHitDto;
import ru.practicum.dto.ViewStatsDto;

import java.time.LocalDateTime;
import java.util.List;

public interface StatsService {
    void createEndpointHit(EndpointHitDto itemDto);

    List<ViewStatsDto> getHitStatistics(LocalDateTime start, LocalDateTime end, List<String> uris, boolean unique);
}