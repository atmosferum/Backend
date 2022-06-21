package ru.whattime.whattime.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Setter
@Getter
@NoArgsConstructor
@Entity
@Table(name = "intervals")
public class Interval extends EntityBase {

    @ManyToOne
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;

    @ManyToOne
    @JoinColumn(name = "event_id", nullable = false)
    private Event event;

    @Column(nullable = false)
    private Long startTimeInEpochSeconds;

    @Column(nullable = false)
    private Long endTimeInEpochSeconds;
}
