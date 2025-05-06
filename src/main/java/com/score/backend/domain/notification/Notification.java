package com.score.backend.domain.notification;

import com.score.backend.config.BaseEntity;
import com.score.backend.domain.group.GroupEntity;
import com.score.backend.domain.user.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Notification extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "varchar(20)", nullable = false)
    @Enumerated(EnumType.STRING)
    private NotificationType type;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User receiver;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @Setter
    private User sender;

    @ManyToOne
    @JoinColumn(name = "group_id")
    private GroupEntity relatedGroup;

    private String title;

    private String body;

    public String getTitle() {
        if (this.getType() == NotificationType.ETC) {
            return this.title;
        }
        switch (this.getType()) {
            case BATON, TAGGED -> {
                return String.format(this.title, this.getSender().getNickname());
            }
            case JOIN_REQUEST -> {
                return String.format(this.title, this.getSender().getNickname(), this.getRelatedGroup().getGroupName());
            }
            case JOIN_COMPLETE -> {
                return String.format(this.title, this.getRelatedGroup().getGroupName(), this.getReceiver().getNickname());
            }
            case GROUP_RANKING -> {
                return String.format(this.title, this.getReceiver().getNickname(), this.getRelatedGroup().getGroupName());
            } case SCHOOL_RANKING -> {
                return String.format(this.title, this.getRelatedGroup().getGroupName(), this.getRelatedGroup().getBelongingSchool().getSchoolName());
            }
            default -> {
                return this.type.getTitle();
            }
        }

    }

    public String getBody() {
        if (this.getType() == NotificationType.ETC) {
            return this.body;
        }
        if (this.getType() == NotificationType.JOIN_COMPLETE) {
            return String.format(this.body, this.getRelatedGroup().getGroupName());
        }
        return this.getType().getBody();
    }
}
