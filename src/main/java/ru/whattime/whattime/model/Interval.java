package ru.whattime.whattime.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "intervals")
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class Interval {
    @Id
    @Column(unique = true, nullable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;

    @ManyToOne
    @JoinColumn(name = "event_id", nullable = false)
    private Event event;

    @Column(nullable = false)
    private long startTimeInEpochSeconds;

    @Column(nullable = false)
    private long endTimeInEpochSeconds;
}
