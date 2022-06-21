package ru.whattime.whattime.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name="users")
public class User extends EntityBase {

    @Column(nullable = false)
    private String name;

    @OneToMany(mappedBy = "owner", fetch = FetchType.LAZY)
    private List<Event> events;
}
