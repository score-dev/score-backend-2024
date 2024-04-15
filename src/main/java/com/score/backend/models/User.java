package com.score.backend.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.score.backend.config.BaseEntity;
import com.score.backend.models.exercise.Exercise;
import jakarta.persistence.*;
import lombok.*;

import java.sql.Time;
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

    @Column(nullable = false)
    private int consecutiveDate; // 며칠 연속으로 운동 중인지?

    @Column(nullable = false)
    private double cumulativeTime; // 누적 운동 시간

    @Column(nullable = false)
    private double cumulativeDistance; // 누적 운동 거리

    @OneToMany(mappedBy="agent")
    @JsonIgnore
    private List<Exercise> feeds = new ArrayList<>();

    private boolean marketing;

    private boolean push;

    private String refreshToken;

    private String loginKey;
}
