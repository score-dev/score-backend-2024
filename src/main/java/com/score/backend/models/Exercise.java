package com.score.backend.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@Entity
@Getter
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "DTYPE")
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
public abstract class Exercise {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "exercise_id")
    private Long id;

    private LocalDateTime startedAt;

    private LocalDateTime completedAt;

    private int reducedKcal;

    private String location;

    private String weather;

    private int temperature;

    private String emotion;

    private String exercisePic;

    private String content;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    public Exercise(LocalDateTime startedAt, LocalDateTime completedAt, int reducedKcal, String location, String weather, int temperature, String emotion, String exercisePic, String content) {
        this.startedAt = startedAt;
        this.completedAt = completedAt;
        this.reducedKcal = reducedKcal;
        this.location = location;
        this.weather = weather;
        this.temperature = temperature;
        this.emotion = emotion;
        this.exercisePic = exercisePic;
        this.content = content;
    }
}
