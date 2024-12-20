package com.score.backend.domain.exercise.emotion;

import java.util.List;

public interface EmotionRepositoryCustom {
    List<Emotion> findAllEmotionType(Long feedId, EmotionType emotionType);
}
