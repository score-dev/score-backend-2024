package com.score.backend.models.dtos;

import com.score.backend.models.exercise.Exercise;
import com.score.backend.models.exercise.Walking;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class WalkingDto {
    private LocalDateTime startedAt;
    private LocalDateTime completedAt;
    private int reducedKcal;
    private String location;
    private String weather;
    private int temperature;
    private String emotion;
    private String exercisePic;
    private String content;

    public Exercise toEntity() {
        return Walking.builder()
                .startedAt(startedAt)
                .completedAt(completedAt)
                .reducedKcal(reducedKcal)
                .location(location)
                .weather(weather)
                .temperature(temperature)
                .emotion(emotion)
                .exercisePic(exercisePic)
                .content(content)
                .build();
    }
}
