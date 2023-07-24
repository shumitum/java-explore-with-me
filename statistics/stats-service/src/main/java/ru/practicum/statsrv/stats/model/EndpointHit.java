package ru.practicum.statsrv.stats.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "statistics", schema = "public")
public class EndpointHit {
    @Id
    @Column(name = "hit_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(name = "app")
    private String app;

    @NotBlank
    @Column(name = "uri")
    private String uri;

    @NotBlank
    @Column(name = "ip")
    private String ip;

    @NotNull
    @Column(name = "created")
    private LocalDateTime timestamp;
}