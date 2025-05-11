package com.score.backend.dtos;

import com.score.backend.domain.notification.Notification;
import com.score.backend.domain.notification.NotificationType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor
public class FcmNotificationResponse {
    @Schema(description = "알림을 식별하는 고유 id 값")
    private Long notificationId;
    @Schema(description = "알림 타입",
            examples = {"GOAL(목표 운동 시간 알림 -> 시스템 알림)",
                    "JOIN_REQUEST(그룹 가입 요청 알림)",
                    "JOIN_COMPLETE(그룹 가입 완료 알림)",
                    "BATON(바통 찌르기 알림)",
                    "GROUP_RANKING(그룹 랭킹 1위 알림 -> 시스템 알림)",
                    "SCHOOL_RANKING(학교 랭킹 1위 알림 -> 시스템 알림)",
                    "TAGGED(함께 운동한 친구 태그 알림)",
                    "ETC(기타)"})
    private NotificationType type;
    @Schema(description = "알림을 보낸 유저의 고유 id 값 (시스템 알림인 경우 null)", nullable = true)
    private Long senderId;
    @Schema(description = "알림을 보낸 사람의 프로필 이미지 url (시스템 알림일 경우 null)", nullable = true)
    private String senderProfileImgUrl;
    @Schema(description = "알림을 받은 유저의 고유 id 값")
    private Long receiverId;
    @Schema(description = "알림이 관련되어 있는 그룹의 고유 id 값 (그룹 관련 알림이 아닌 경우 null)", nullable = true)
    private Long relatedGroupId;
    @Schema(description = "알림을 받은 유저가 태그된 피드의 고유 id 값 (태그 알림이 아닌 경우 null)", nullable = true)
    private Long relatedFeedId;
    @Schema(description = "알림이 전송된 날짜 및 시각")
    private LocalDateTime createdAt;
    @Schema(description = "알림 제목")
    private String title;
    @Schema(description = "알림 내용")
    private String body;
    @Schema(description = "확인된 알림인지 여부")
    private boolean isRead;
    @Schema(description = "해당 그룹 가입 요청을 승인했는지 여부 (그룹 관련 알림이 아니거나 아직 확인되지 않은 알림인 경우 null)", nullable = true)
    private boolean isJoinRequestAccepted;

    public Page<FcmNotificationResponse> toDto(Page<Notification> notificationPages) {
        List<FcmNotificationResponse> contents = notificationPages.getContent().stream()
                .map(notification -> FcmNotificationResponse.builder()
                        .notificationId(notification.getId())
                        .type(notification.getType())
                        .senderId(notification.getSender().getId().equals(1L)? null : notification.getSender().getId())
                        .receiverId(notification.getReceiver().getId())
                        .senderProfileImgUrl(notification.getSender().getProfileImg() != null ? notification.getSender().getProfileImg() : null)
                        .relatedGroupId(notification.getRelatedGroup() != null ? notification.getRelatedGroup().getGroupId() : null)
                        .relatedFeedId(notification.getRelatedFeed() != null ? notification.getRelatedFeed().getId() : null)
                        .createdAt(notification.getCreatedAt())
                        .title(notification.getTitle())
                        .body(notification.getBody())
                        .isRead(notification.isRead())
                        .isJoinRequestAccepted(notification.isJoinRequestAccepted())
                        .build()).toList();
        return new PageImpl<>(contents, notificationPages.getPageable(), notificationPages.getTotalElements());
    }
}
