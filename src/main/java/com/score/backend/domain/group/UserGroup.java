package com.score.backend.domain.group;

import com.score.backend.config.BaseEntity;
import com.score.backend.domain.user.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class UserGroup extends BaseEntity {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User member;

    @ManyToOne
    @JoinColumn(name = "group_id")
    private GroupEntity group;

    @CreatedDate
    private LocalDateTime joinedAt;
}
