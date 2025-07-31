package com.score.backend.domain.exercise.emotion;

import com.score.backend.domain.exercise.Exercise;
import com.score.backend.dtos.EmotionStatusResponse;
import com.score.backend.exceptions.ExceptionType;
import com.score.backend.exceptions.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class EmotionService {
    private final EmotionRepository emotionRepository;

    public boolean addOrDeleteEmotion(Long agentId, Long feedId, EmotionType type) {
        int deleted = emotionRepository.deleteEmotionNative(agentId, feedId, type.name());
        if (deleted == 0) {
            emotionRepository.insertIfNotDeleted(agentId, feedId, type.name());
            return true;
        }
        return false;
    }

    // 피드에 남겨져 있는 모든 감정 표현 삭제
    public void deleteAllEmotions(Exercise feed) {
        emotionRepository.deleteAll(findAll(feed.getId()));
    }

    // 피드에 남겨져 있는 모든 감정 표현 조회 (emotionType 상관 없이 모두 조회)
    private List<Emotion> findAll(Long feedId) {
        return emotionRepository.findAllEmotionsByFeedId(feedId).orElseThrow(
                () -> new NotFoundException(ExceptionType.FEED_NOT_FOUND)
        );
    }

    @Transactional(readOnly = true)
    public List<EmotionStatusResponse> makeEmotionListDto(Long feedId) {
        List<Emotion> emotions = findAll(feedId);
        List<EmotionStatusResponse> dto = new ArrayList<>();
        for (Emotion emotion : emotions) {
            dto.add(EmotionStatusResponse.of(emotion.getAgent(), emotion));
        }
        return dto;
    }

    // feedId의 피드에 남겨져 있는 emotionType 타입의 감정 표현 목록 조회
    @Transactional(readOnly = true)
    public List<Emotion> findAllEmotionTypes(Long feedId, EmotionType emotionType) {
        return emotionRepository.findAllEmotionType(feedId, emotionType);
    }
}
