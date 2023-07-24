package ru.practicum.statsrv.stats;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.dto.ViewStatsDto;
import ru.practicum.statsrv.stats.model.EndpointHit;

import java.time.LocalDateTime;
import java.util.List;

public interface StatsRepository extends JpaRepository<EndpointHit, Long> {
    @Query("select new ru.practicum.dto.ViewStatsDto(app, uri, count(ip)) from EndpointHit " +
            "WHERE timestamp between :start AND :end " +
            "AND ((:uris) is NULL or uri in :uris) " +
            "group by app, uri " +
            "order by count(ip) desc")
    List<ViewStatsDto> getHitStatistics(@Param("start") LocalDateTime start,
                                        @Param("end") LocalDateTime end,
                                        @Param("uris") List<String> uris);

    @Query("select new ru.practicum.dto.ViewStatsDto(app, uri, count(distinct uri)) from EndpointHit " +
            "WHERE timestamp BETWEEN :start AND :end " +
            "AND ((:uris) is NULL or uri in :uris) " +
            "group by app, uri " +
            "order by count(ip) desc")
    List<ViewStatsDto> getHitStatisticsWithUniqueIp(@Param("start") LocalDateTime start,
                                                    @Param("end") LocalDateTime end,
                                                    @Param("uris") List<String> uris);
}