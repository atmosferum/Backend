package ru.whattime.whattime.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "event")
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class Event {
    @Id
    private Long id;

    @Type(type = "org.hibernate.type.UUIDCharType")
    @Column(unique = true, nullable = false)
    private UUID uuid;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String description;

    @ManyToOne
    private User owner;

    @Column(nullable = false)
    private Long createdAt;

}
