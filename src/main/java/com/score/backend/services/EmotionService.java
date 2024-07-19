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

    // 피드에 새로운 감정 표현 추가
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

    // 감정 표현 삭제
    public void deleteEmotion(Emotion emotion) {
        emotionRepository.delete(emotion);
    }

    // 피드에 남겨져 있는 모든 감정 표현 조회 (emotionType 상관 없이 모두 조회)
    @Transactional(readOnly = true)
    public List<Emotion> findAll(Long feedId) {
        return emotionRepository.findAllEmotionsByFeedId(feedId).orElseThrow(
                () -> new RuntimeException("Feed Not Found")
        );
    }

    // feedId의 피드에 남겨져 있는 emotionType 타입의 감정 표현 목록 조회
    @Transactional(readOnly = true)
    public List<Emotion> findAllEmotionTypes(Long feedId, EmotionType emotionType) {
        return emotionRepository.findAllEmotionType(feedId, emotionType);
    }

    // 해당 타입의 감정 표현을 이미 한 유저인지 여부 확인
    @Transactional(readOnly = true)
    public Emotion checkDuplicateEmotion(Long userId, List<Emotion> savedEmotions) {
        if (savedEmotions.isEmpty()) {
            return null;
        }
        for (Emotion emotion : savedEmotions) {
            if (emotion.getAgent().getId().equals(userId)) {
                return emotion;
            }
        }
        return null;
    }
}