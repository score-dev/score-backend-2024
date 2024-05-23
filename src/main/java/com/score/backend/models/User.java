package com.score.backend.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.score.backend.config.BaseEntity;
import com.score.backend.models.exercise.Exercise;
import jakarta.persistence.*;
import lombok.*;

import java.sql.Time;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "user_id")
    private Long id;

    @Column(nullable = false, unique = true)
    private String nickname;

    private int grade;

    private int height;

    private int weight;

    private String profileImg;

    private Time goal;

    private int level;

    private int point;

    private LocalDateTime lastExerciseDateTime;

    @Column(nullable = false)
    private int consecutiveDate; // 며칠 연속으로 운동 중인지?

    @Column(nullable = false)
    private double cumulativeTime; // 누적 운동 시간

    @Column(nullable = false)
    private double cumulativeDistance; // 누적 운동 거리

    @OneToMany(mappedBy="agent")
    @JsonIgnore
    private final List<Exercise> feeds = new ArrayList<>();

    @OneToMany
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private List<User> mates = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name="group_id")
    private Group group;

    private boolean marketing;

    private boolean push;

    private String refreshToken;

    private String loginKey;

    public void setGroup(Group group) {
        this.group = group;
    }

    public void updateCumulativeTime(double duration) {
        this.cumulativeTime += duration;
    }
    public void updateCumulativeDistance(double distance) {
        this.cumulativeDistance += distance;
    }
    public void updatePoint(int point) {
        this.point += point;
    }
    public void initPoint(int point) {
        this.point = point;
    }
    public void updateConsecutiveDate(boolean isIncrement) {
        if (isIncrement) {
            this.consecutiveDate++;
        } else {
            this.consecutiveDate = 0;
        }
    }
    public void updateLastExerciseDateTime(LocalDateTime lastExerciseDateTime) {this.lastExerciseDateTime = lastExerciseDateTime;}
    public void increaseLevel(int amount) {
        this.level = this.level + amount;
    }
    public void setProfileImageUrl(String profileImg) {
        this.profileImg = profileImg;
    }
}

