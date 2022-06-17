package ru.whattime.whattime.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "events")
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class Event {

    @Id
    @Column(nullable = false)
    private String uuid;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String description;

    @ManyToOne
    private User owner;

    @Column(nullable = false)
    private Long createdAt;

}
