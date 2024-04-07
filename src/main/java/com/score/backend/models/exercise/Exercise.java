package com.score.backend.models.exercise;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.score.backend.models.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "DTYPE")
@NoArgsConstructor
public abstract class Exercise {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "exercise_id")
    private Long id;

    @ManyToOne @JoinColumn(name="user_id")
    private User agent; // 피드를 업로드한 유저

    @OneToMany(mappedBy = "exercise")
    @JsonIgnore
    private List<ExerciseUser> exerciseUsers = new ArrayList<>(); // 함께 운동한 유저

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
