package ru.practicum.statsrv.stats;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.EndpointHitDto;
import ru.practicum.dto.ViewStatsDto;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class StatsController {
    private final StatsService statsService;

    @PostMapping("/hit")
    @ResponseStatus(HttpStatus.CREATED)
    public void createEndpointHit(@RequestBody @Valid EndpointHitDto endpointHitDto) {
        statsService.createEndpointHit(endpointHitDto);
        log.info("Cовершен запрос к сервису:{}, uri:{}, с IP:{}, точное время запроса: {}", endpointHitDto.getApp(),
                endpointHitDto.getUri(), endpointHitDto.getIp(), endpointHitDto.getTimestamp());
    }

    @GetMapping("/stats")
    @ResponseStatus(HttpStatus.OK)
    public List<ViewStatsDto> getHitStatistics(@RequestParam LocalDateTime start,
                                               @RequestParam LocalDateTime end,
                                               @RequestParam(required = false) List<String> uris,
                                               @RequestParam(defaultValue = "false") boolean unique) {
        log.info("Запрос статистики обращений к сервисам с {} по {}, по адресу(ам): {}, только уникальные?: {}",
                start, end, uris, unique);
        return statsService.getHitStatistics(start, end, uris, unique);
    }
}