package com.score.backend.models.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Schema(description = "유저의 알림 수신 동의 여부를 응답받기 위한 DTO입니다.")
@Getter
public class NotificationStatusRequest {
    @Schema(description = "유저의 id 깂")
    private Long userId;
    @Schema(description = "마케팅 푸시 수신 동의 여부")
    private boolean marketing;
    @Schema(description = "목표 운동 시간 알림 수신 동의 여부")
    private boolean exercisingTime;
    @Schema(description = "소통(태그) 알림 수신 동의 여부")
    private boolean tag;
}
