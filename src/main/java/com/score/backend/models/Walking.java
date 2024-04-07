package com.score.backend.models;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@DiscriminatorValue("Walk")
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Walking extends Exercise {
    private int distance;

    @Builder
    public Walking(LocalDateTime startedAt, LocalDateTime completedAt, int reducedKcal, String location, String weather, int temperature, String emotion, String exercisePic, String content, int distance) {
        super(startedAt, completedAt, reducedKcal, location, weather, temperature, emotion, exercisePic, content);
        this.distance = distance;
    }

    @Builder
    public Walking(LocalDateTime startedAt, LocalDateTime completedAt, int reducedKcal, String location, String weather, int temperature, String emotion, String content, int distance) {
        super(startedAt, completedAt, reducedKcal, location, weather, temperature, emotion, content);
        this.distance = distance;
    }
}
