package com.score.backend.domain.exercise.emotion;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.PessimisticLockingFailureException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmotionFacade {
    private final EmotionService emotionService;

    // 새로운 감정 추가 시도 -> 유니크 제약 조건에 위배되면 이미 감정 표현이 존재한다는 뜻이므로 삭제
    public boolean safeAddOrDeleteEmotion(Long agentId, Long feedId, EmotionType type) {
        for (int i = 0; i < 3; i++) {
            try {
                emotionService.addEmotion(agentId, feedId, type);
                return true;
            } catch (DataIntegrityViolationException e) {
                emotionService.deleteEmotion(agentId, feedId, type);
                return false;
            } catch (PessimisticLockingFailureException e) {
                if (i == 2) throw e;
            }
        }
        return false;
    }
}
