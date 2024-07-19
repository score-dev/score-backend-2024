package com.score.backend.repositories.emotion;

import com.score.backend.models.Emotion;
import com.score.backend.models.enums.EmotionType;

import java.util.List;

public interface EmotionRepositoryCustom {
    List<Emotion> findAllEmotionType(Long feedId, EmotionType emotionType);
}
