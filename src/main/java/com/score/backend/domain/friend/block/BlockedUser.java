package com.score.backend.domain.friend.block;

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
public class BlockedUser extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "blocker_id", nullable = false)
    private User blocker;

    @ManyToOne
    @JoinColumn(name = "blocked_id", nullable = false)
    private User blocked;

    public BlockedUser(User blocker, User blocked) {
        if (blocker.getId().equals(blocked.getId())) {
            throw new ScoreCustomException(ExceptionType.SELF_BLOCK);
        }
        this.blocker = blocker;
        this.blocked = blocked;
    }
}
