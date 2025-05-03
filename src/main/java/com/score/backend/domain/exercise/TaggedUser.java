package com.score.backend.domain.exercise;

import com.score.backend.domain.user.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TaggedUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "exercise_id")
    @Setter
    private Exercise exercise;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public TaggedUser(User user) {
        this.user = user;
    }
}
