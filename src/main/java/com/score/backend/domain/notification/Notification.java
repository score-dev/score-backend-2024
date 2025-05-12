package com.score.backend.domain.notification;

import com.score.backend.config.BaseEntity;
import com.score.backend.domain.exercise.Exercise;
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

    @Column(name = "notification_type", columnDefinition = "varchar(20)", nullable = false)
    @Enumerated(EnumType.STRING)
    private NotificationType type;

    @ManyToOne
    @JoinColumn(name = "receiver_id")
    private User receiver;

    @ManyToOne
    @JoinColumn(name = "sender_id")
    @Setter
    private User sender;

    @ManyToOne
    @JoinColumn(name = "related_group")
    private GroupEntity relatedGroup;

    @ManyToOne
    @JoinColumn(name = "related_feed")
    private Exercise relatedFeed;

    private String title;

    private String body;

    @Setter
    @Builder.Default
    private boolean isRead = false;

    @Setter
    @Builder.Default
    private boolean isJoinRequestAccepted = false;

    public String getTitle() {
        if (this.getType() == NotificationType.ETC) {
            return this.title;
        }
        switch (this.getType()) {
            case BATON, TAGGED -> {
                return String.format(this.getType().getTitle(), this.getSender().getNickname());
            }
            case JOIN_REQUEST -> {
                return String.format(this.getType().getTitle(), this.getSender().getNickname(), this.getRelatedGroup().getGroupName());
            }
            case JOIN_COMPLETE -> {
                return String.format(this.getType().getTitle(), this.getRelatedGroup().getGroupName(), this.getReceiver().getNickname());
            }
            case GROUP_RANKING -> {
                return String.format(this.getType().getTitle(), this.getReceiver().getNickname(), this.getRelatedGroup().getGroupName());
            } case SCHOOL_RANKING -> {
                return String.format(this.getType().getTitle(), this.getRelatedGroup().getGroupName(), this.getRelatedGroup().getBelongingSchool().getSchoolName());
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
            return String.format(this.getType().getBody(), this.getRelatedGroup().getGroupName());
        }
        return this.getType().getBody();
    }
}
