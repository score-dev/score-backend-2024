package com.score.backend.domain.friend.block;

import com.score.backend.config.BaseEntity;
import com.score.backend.domain.user.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.coyote.BadRequestException;

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

    public BlockedUser(User blocker, User blocked) throws BadRequestException {
        if (blocker.getId().equals(blocked.getId())) {
            throw new BadRequestException("자기 자신을 차단할 수 없습니다.");
        }
        this.blocker = blocker;
        this.blocked = blocked;
    }
}
