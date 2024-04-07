package com.score.backend.models.exercise;

import com.score.backend.models.User;
import jakarta.persistence.*;

// 운동과 함께 운동한 유저간의 다대다 연관 관계 중간 테이블
@Entity
@IdClass(ExerciseUserId.class)
public class ExerciseUser {
    @Id
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Id
    @ManyToOne
    @JoinColumn(name = "exercise_id")
    private Exercise exercise;
}
