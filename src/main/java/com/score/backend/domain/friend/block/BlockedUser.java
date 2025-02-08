package com.score.backend.domain.friend.block;

import com.score.backend.config.BaseEntity;
import com.score.backend.domain.user.User;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
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
        this.blocker = blocker;
        this.blocked = blocked;
    }
}
