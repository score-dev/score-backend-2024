package com.score.backend.models.exercise;

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
    private double distance;

    @Builder
    public Walking(LocalDateTime startedAt, LocalDateTime completedAt, int reducedKcal, String location, String weather, int temperature, String fineDust, String feeling, String exercisePic, double distance) {
        super(startedAt, completedAt, reducedKcal, location, weather, temperature, fineDust, feeling, exercisePic);
        this.distance = distance;
    }
}
