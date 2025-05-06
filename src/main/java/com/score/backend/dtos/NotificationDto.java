package com.score.backend.dtos;

import com.score.backend.domain.exercise.Exercise;
import com.score.backend.domain.group.GroupEntity;
import com.score.backend.domain.notification.NotificationType;
import com.score.backend.domain.user.User;
import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class NotificationDto {
    private User sender;
    private User receiver;
    private GroupEntity relatedGroup;
    private Exercise relatedFeed;
    private NotificationType type;
    private String title;
    private String body;
}
