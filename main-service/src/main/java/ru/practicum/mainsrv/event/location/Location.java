package ru.practicum.mainsrv.event.location;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Data
@Builder
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class Location {
    @Column(name = "lat")
    private Float lat;

    @Column(name = "lon")
    private Float lon;
}