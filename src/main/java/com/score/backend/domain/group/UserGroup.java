package com.score.backend.domain.group;

import com.score.backend.config.BaseEntity;
import com.score.backend.domain.user.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserGroup extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User member;

    @ManyToOne
    @JoinColumn(name = "group_id")
    private GroupEntity group;

    @CreatedDate
    private LocalDateTime joinedAt;

    public UserGroup(User member, GroupEntity group) {
        this.member = member;
        this.group = group;
    }
}
