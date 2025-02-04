package com.score.backend.domain.exercise;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.score.backend.config.BaseEntity;
import com.score.backend.domain.exercise.emotion.Emotion;
import com.score.backend.domain.user.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "DTYPE")
@NoArgsConstructor
public abstract class Exercise extends BaseEntity {
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

    private String fineDust;

    private int temperature;

    private String feeling;

    private String exercisePic;

    private final List<Emotion> emotions = new ArrayList<>(); // 피드에 추가된 감정 표현 리스트

    private void setAgent(User agent) {
        this.agent = agent;
        agent.getFeeds().add(this);
    }

    private void setExerciseUser(ExerciseUser exerciseUser) {
        exerciseUsers.add(exerciseUser);
        exerciseUser.setExercise(this);
    }

    public void setAgentAndExerciseUser(User agent, List<ExerciseUser> exerciseUsers) {
        this.setAgent(agent);
        for (ExerciseUser exerciseUser : exerciseUsers) {
            this.setExerciseUser(exerciseUser);
        }
    }

    public void setExercisePicUrl(String exercisePicUrl) {
        this.exercisePic = exercisePicUrl;
    }

    public Exercise(LocalDateTime startedAt, LocalDateTime completedAt, int reducedKcal, String location, String weather, int temperature, String fineDust, String feeling, String exercisePic) {
        this.startedAt = startedAt;
        this.completedAt = completedAt;
        this.reducedKcal = reducedKcal;
        this.location = location;
        this.weather = weather;
        this.temperature = temperature;
        this.fineDust = fineDust;
        this.feeling = feeling;
        this.exercisePic = exercisePic;
    }
}
