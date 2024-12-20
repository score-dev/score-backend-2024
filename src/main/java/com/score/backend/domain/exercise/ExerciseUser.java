package com.score.backend.domain.exercise;

import com.score.backend.domain.user.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

// 운동과 함께 운동한 유저간의 다대다 연관 관계 중간 테이블
@Entity
@Getter
@IdClass(ExerciseUserId.class)
public class ExerciseUser {
    @Id
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Id
    @ManyToOne
    @JoinColumn(name = "exercise_id")
    @Setter(AccessLevel.PROTECTED)
    private Exercise exercise;

    public ExerciseUser(User user) {
        this.user = user;
    }

    public ExerciseUser() {

    }
}
