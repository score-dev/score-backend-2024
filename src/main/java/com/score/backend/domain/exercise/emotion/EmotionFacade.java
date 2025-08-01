package com.score.backend.domain.exercise.emotion;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.PessimisticLockingFailureException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmotionFacade {
    private final EmotionService emotionService;

    public boolean safeAddOrDeleteEmotion(Long agentId, Long feedId, EmotionType type) {
        for (int i = 0; i < 3; i++) {
            try {
                return emotionService.addOrDeleteEmotion(agentId, feedId, type);
            } catch (PessimisticLockingFailureException e) {
                if (i == 2) throw e;
            }
        }
        return false;
    }
}
