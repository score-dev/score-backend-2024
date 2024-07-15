package com.score.backend.services;

import com.score.backend.models.Emotion;
import com.score.backend.models.User;
import com.score.backend.models.enums.EmotionType;
import com.score.backend.models.exercise.Exercise;
import com.score.backend.repositories.emotion.EmotionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class EmotionService {
    private final UserService userService;
    private final ExerciseService exerciseService;
    private final EmotionRepository emotionRepository;

    // 새로운 피드 추가
    public void addEmotion(Long agentId, Long feedId, EmotionType emotionType) {
        User agent = userService.findUserById(agentId).orElseThrow(
                () -> new RuntimeException("Agent Not Found")
        );
        Exercise feed = exerciseService.findFeedByExerciseId(feedId).orElseThrow(
                () -> new RuntimeException("Feed Not Found")
        );
        Emotion emotion = new Emotion();
        emotion.setEmotion(agent, feed, emotionType);
        emotionRepository.save(emotion);
    }

    // feedId의 피드에 남겨져 있는 emotionType 타입의 감정 표현 목록 조회
    public List<Emotion> findAllEmotions(Long feedId, EmotionType emotionType) {
        return emotionRepository.findAllByEmotionType(feedId, emotionType);
    }
}