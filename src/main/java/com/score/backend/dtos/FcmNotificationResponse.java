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
    private Long id;
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
    @Schema(description = "알림이 전송된 날짜 및 시각")
    private LocalDateTime createdAt;
    @Schema(description = "알림 제목")
    private String title;
    @Schema(description = "알림 내용")
    private String body;
    @Schema(description = "알림을 보낸 사람의 프로필 이미지 url (시스템 알림일 경우 null)", nullable = true)
    private String senderProfileImgUrl;

    public Page<FcmNotificationResponse> toDto(Page<Notification> notificationPages) {
        List<FcmNotificationResponse> contents = notificationPages.getContent().stream()
                .map(notification -> FcmNotificationResponse.builder()
                        .id(notification.getId())
                        .type(notification.getType())
                        .createdAt(notification.getCreatedAt())
                        .title(notification.getTitle())
                        .body(notification.getBody())
                        .senderProfileImgUrl(notification.getSender().getProfileImg())
                        .build()).toList();
        return new PageImpl<>(contents, notificationPages.getPageable(), notificationPages.getTotalElements());
    }
}
