package com.score.backend.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@Schema(description = "마이페이지 운동 기록 캘린더 정보 응답을 위한 DTO")
public class FeedCalendarResponse {
    @Schema(description = "피드를 식별할 수 있는 고유 id")
    private Long feedId;
    @Schema(description = "운동 시작 시간")
    private LocalDateTime startedAt;
    @Schema(description = "운동을 끝낸 시간")
    private LocalDateTime completedAt;
}
