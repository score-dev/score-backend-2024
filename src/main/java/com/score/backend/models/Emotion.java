package com.score.backend.models;

import com.score.backend.config.BaseEntity;
import com.score.backend.models.enums.EmotionType;
import com.score.backend.models.exercise.Exercise;
import jakarta.persistence.*;
import lombok.Getter;
import org.hibernate.annotations.JdbcType;
import org.hibernate.type.descriptor.jdbc.SmallIntJdbcType;

@Entity
@Getter
public class Emotion extends BaseEntity {
    @Id @GeneratedValue
    @Column(name = "emotion_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User agent; // 이 감정 표현을 추가한 사람

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "exercise_id")
    private Exercise feed; // 이 감정 표현이 추가된 피드

    @JdbcType(value = SmallIntJdbcType.class)
    private EmotionType emotionType; // 어떤 감정 표현인지?

    public void setFeed(Exercise feed) {
        feed.getEmotions().add(this);
    }
}
