package ru.practicum.mainsrv.event;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import ru.practicum.mainsrv.category.Category;
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
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "events", schema = "public")
public class Event {
    @Id
    @Column(name = "event_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @NotBlank
    @Column(name = "annotation")
    String annotation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", referencedColumnName = "category_id")
    @ToString.Exclude
    Category category;

/*    @Column(name = "confirmed_requests")
    int confirmedRequests;*/

    @NotNull
    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_on")
    LocalDateTime createdOn;

    @NotBlank
    @Column(name = "description")
    String description;

    @NotNull
    @Column(name = "event_date")
    LocalDateTime eventDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "initiator_id", referencedColumnName = "user_id")
    @ToString.Exclude
    User initiator;

    @Embedded
    Location location;

    @NotNull
    @Column(name = "paid")
    Boolean paid;

    @Column(name = "participant_limit")
    int participantLimit;

    //@NotNull
    @Column(name = "published_on")
    LocalDateTime publishedOn;

    @Column(name = "request_moderation")
    Boolean requestModeration;

    @NotNull
    @Column(name = "state")
    @Enumerated(EnumType.STRING)
    EventState state;

    @NotBlank
    @Column(name = "title")
    String title;
}
