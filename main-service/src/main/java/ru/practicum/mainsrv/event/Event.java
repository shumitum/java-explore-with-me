package ru.practicum.mainsrv.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import ru.practicum.mainsrv.category.Category;
import ru.practicum.mainsrv.event.enums.EventState;
import ru.practicum.mainsrv.event.location.Location;
import ru.practicum.mainsrv.user.User;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "events", schema = "public")
public class Event {
    @Id
    @Column(name = "event_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(name = "annotation")
    private String annotation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", referencedColumnName = "category_id")
    @ToString.Exclude
    private Category category;

    @Column(name = "confirmed_requests")
    private Long confirmedRequests;

    @NotNull
    @Column(name = "created_on")
    private LocalDateTime createdOn;

    @NotBlank
    @Column(name = "description")
    private String description;

    @NotNull
    @Column(name = "event_date")
    private LocalDateTime eventDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "initiator_id", referencedColumnName = "user_id")
    @ToString.Exclude
    private User initiator;

    @Embedded
    private Location location;

    @NotNull
    @Column(name = "paid")
    private Boolean paid;

    @Column(name = "participant_limit")
    private int participantLimit;

    @Column(name = "published_on")
    private LocalDateTime publishedOn;

    @Column(name = "request_moderation")
    private Boolean requestModeration;

    @NotNull
    @Column(name = "state")
    @Enumerated(EnumType.STRING)
    private EventState state;

    @NotBlank
    @Column(name = "title")
    private String title;

    @Column(name = "views")
    private long views;
}