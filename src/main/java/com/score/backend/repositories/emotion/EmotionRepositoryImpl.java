package com.score.backend.repositories.emotion;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.score.backend.models.Emotion;
import com.score.backend.models.QEmotion;
import com.score.backend.models.enums.EmotionType;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class EmotionRepositoryImpl implements EmotionRepositoryCustom {
    private final JPAQueryFactory queryFactory;
    QEmotion e = new QEmotion("e");

    @Override
    public List<Emotion> findAllByEmotionType(Long feedId, EmotionType emotionType) {
        return queryFactory
                .selectFrom(e)
                .where(e.feed.id.eq(feedId).and(e.emotionType.eq(emotionType)))
                .fetch();
    }
}
