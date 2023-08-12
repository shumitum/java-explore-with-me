package ru.practicum.client;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.dto.EndpointHitDto;
import ru.practicum.dto.ViewStatsDto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

import static ru.practicum.dto.EndpointHitDto.DATE_TIME_PATTERN;

@Service
public class StatsClient extends BaseClient {

    @Autowired
    public StatsClient(@Value("${stats-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(builder
                .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl))
                .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                .build()
        );
    }

    public void createEndpointHit(EndpointHitDto endpointHitDto) {
        post("/hit", endpointHitDto);
    }

    public List<ViewStatsDto> getHitStatistics(@RequestParam LocalDateTime start,
                                               @RequestParam LocalDateTime end,
                                               @RequestParam(required = false) List<String> uris,
                                               @RequestParam(defaultValue = "false") boolean unique) {
        Map<String, Object> parameters = Map.of(
                "start", start.format(DateTimeFormatter.ofPattern(DATE_TIME_PATTERN)),
                "end", end.format(DateTimeFormatter.ofPattern(DATE_TIME_PATTERN)),
                "uris", String.join(",", uris),
                "unique", unique
        );
        String path = "/stats?start={start}&end={end}&uris={uris}&unique={unique}";
        return new ObjectMapper().convertValue(get(path, parameters).getBody(), new TypeReference<>() {
        });
    }
}