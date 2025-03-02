package com.score.backend.domain.friend;

import com.score.backend.config.BaseEntity;
import com.score.backend.domain.user.User;
import com.score.backend.exceptions.ExceptionType;
import com.score.backend.exceptions.ScoreCustomException;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Friend extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "friend_id", nullable = false)
    private User friend;

    public Friend(User user, User friend) {
        if (user.getId() == friend.getId()) {
            throw new ScoreCustomException(ExceptionType.SELF_FRIEND);
        }
        this.user = user;
        this.friend = friend;
    }
}
