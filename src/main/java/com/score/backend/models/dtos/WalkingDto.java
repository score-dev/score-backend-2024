package com.score.backend.models.dtos;

import com.score.backend.models.exercise.Exercise;
import com.score.backend.models.exercise.Walking;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Schema(description = "걷기 운동 기록을 저장하기 위한 DTO")
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class WalkingDto {
    @Schema(description = "운동을 시작한 시각")
    private LocalDateTime startedAt;
    @Schema(description = "운동을 끝낸 시각")
    private LocalDateTime completedAt;
    @Schema(description = "운동 기록을 한 유저의 고유 id 값")
    private Long agentId;
    @Schema(description = "agent가 함께 운동했다고 선택한 유저들의 고유 id 값")
    private List<Long> othersId;
    @Schema(description = "걸은 거리")
    private double distance;
    @Schema(description = "운동한 동안 소모한 칼로리")
    private int reducedKcal;
    @Schema(description = "운동한 장소")
    private String location;
    @Schema(description = "날씨")
    private String weather;
    @Schema(description = "기온")
    private int temperature;
    @Schema(description = "오늘의 감정")
    private String emotion;
    @Schema(description = "피드에 업로드할 내용")
    private String content;

    public Exercise toEntity() {
        return Walking.builder()
                .startedAt(startedAt)
                .completedAt(completedAt)
                .reducedKcal(reducedKcal)
                .distance(distance)
                .location(location)
                .weather(weather)
                .temperature(temperature)
                .emotion(emotion)
                .content(content)
                .build();
    }
}
