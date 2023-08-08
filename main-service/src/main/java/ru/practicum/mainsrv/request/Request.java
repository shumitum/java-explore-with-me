package ru.practicum.mainsrv.request;

import lombok.*;
import ru.practicum.mainsrv.event.Event;
import ru.practicum.mainsrv.user.User;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "requests", schema = "public")
public class Request {
    @Id
    @Column(name = "request_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id", referencedColumnName = "event_id")
    @ToString.Exclude
    private Event event;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "requester_id", referencedColumnName = "user_id")
    @ToString.Exclude
    private User requester;

    @NotNull
    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private RequestStatus status;

    @NotNull
    @Builder.Default
    @Column(name = "created")
    private LocalDateTime created = LocalDateTime.now();
}