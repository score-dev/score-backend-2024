package com.score.backend.dtos;

import com.score.backend.domain.notification.Notification;
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
    @Schema(description = "알림이 전송된 날짜 및 시각")
    private LocalDateTime createdAt;
    @Schema(description = "알림 제목")
    private String title;
    @Schema(description = "알림 내용")
    private String body;

    public Page<FcmNotificationResponse> toDto(Page<Notification> notificationPages) {
        List<FcmNotificationResponse> contents = notificationPages.getContent().stream()
                .map(notification -> FcmNotificationResponse.builder()
                        .id(notification.getId())
                        .createdAt(notification.getCreatedAt())
                        .title(notification.getTitle())
                        .body(notification.getBody())
                        .build()).toList();
        return new PageImpl<>(contents, notificationPages.getPageable(), notificationPages.getTotalElements());
    }
}
