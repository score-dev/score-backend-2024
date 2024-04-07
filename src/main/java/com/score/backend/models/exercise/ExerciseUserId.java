package com.score.backend.models.exercise;

import java.io.Serializable;

// ExerciseUser 엔티티의 복합키 사용을 위한 식별자 클래스
public class ExerciseUserId implements Serializable {
    private String user;
    private String exercise;

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }
}
