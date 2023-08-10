package ru.practicum.mainsrv.comment;

import lombok.*;
import ru.practicum.mainsrv.event.Event;
import ru.practicum.mainsrv.user.User;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Data
@Entity
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
@Table(name = "comments", schema = "public")
public class Comment {
    @Id
    @Column(name = "comment_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 1000)
    @Column(name = "text")
    private String text;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", referencedColumnName = "user_id")
    @ToString.Exclude
    private User author;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id", referencedColumnName = "event_id")
    @ToString.Exclude
    private Event event;

    @Builder.Default
    @Column(name = "visible")
    private Boolean visible = true;

    @Builder.Default
    @Column(name = "created")
    private LocalDateTime created = LocalDateTime.now();

    @Enumerated(EnumType.STRING)
    @Column(name = "reason_for_hiding")
    private ReasonForHiding reason;
}