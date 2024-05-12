package com.score.backend.models.dtos;

import com.score.backend.models.exercise.Exercise;
import com.score.backend.models.exercise.Walking;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
public class WalkingDto {
    private LocalDateTime startedAt;
    private LocalDateTime completedAt;
    private Long agentId;
    private List<Long> othersId;
    private double distance;
    private int reducedKcal;
    private String location;
    private String weather;
    private int temperature;
    private String emotion;
    private MultipartFile exercisePic;
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
