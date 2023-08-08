package ru.practicum.statsrv.stats;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.dto.EndpointHitDto;
import ru.practicum.dto.ViewStatsDto;
import ru.practicum.statsrv.exception.TimeValidationException;
import ru.practicum.statsrv.stats.mapper.EndpointHitMapper;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StatsServiceImpl implements StatsService {
    private final StatsRepository statsRepository;
    private final EndpointHitMapper endpointHitMapper;

    @Override
    @Transactional
    public void createEndpointHit(EndpointHitDto endpointHitDto) {
        statsRepository.save(endpointHitMapper.toEndpointHit(endpointHitDto));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ViewStatsDto> getHitStatistics(LocalDateTime start, LocalDateTime end, List<String> uris, boolean unique) {
        if (start != null && end != null && (end.isBefore(start))) {
            throw new TimeValidationException("Параметр дата и время начала выборки должен быть раньше даты и времени конца");
        }
        if (unique) {
            return statsRepository.getHitStatisticsWithUniqueIp(start, end, uris);
        }
        return statsRepository.getHitStatistics(start, end, uris);
    }
}