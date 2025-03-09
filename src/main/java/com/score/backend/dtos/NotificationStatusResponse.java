package com.score.backend.dtos;

import com.score.backend.domain.user.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Schema(description = "유저의 각 알림 수신 동의 현황을 응답하기 위한 DTO입니다.")
@AllArgsConstructor
@Getter
public class NotificationStatusResponse {
    @Schema(description = "유저의 id 깂")
    private Long userId;
    @Schema(description = "마케팅 푸시 수신 동의 여부")
    private boolean marketing;
    @Schema(description = "목표 운동 시간 알림 수신 동의 여부")
    private boolean exercisingTime;
    @Schema(description = "소통(태그) 알림 수신 동의 여부")
    private boolean tag;

    public static NotificationStatusResponse of(User user) {
        return new NotificationStatusResponse(user.getId(), user.isMarketing(), user.isExercisingTime(), user.isTag());
    }
}
