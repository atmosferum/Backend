package ru.whattime.whattime.model;


import lombok.*;

import javax.persistence.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class Interval {
    @Id
    @Column(unique = true, nullable = false)
    private String uuid;

    @ManyToOne
    @JoinColumn(name = "id")
    private User owner;

    @Column(nullable = false)
    private long startTimeInEpochSeconds;

    @Column(nullable = false)
    private long endTimeInEpochSeconds;
}
