package com.score.backend.domain.exercise.emotion;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class EmotionRepositoryImpl implements EmotionRepositoryCustom {
    private final JPAQueryFactory queryFactory;
    QEmotion e = new QEmotion("e");

    @Override
    public List<Emotion> findAllEmotionType(Long feedId, EmotionType emotionType) {
        return queryFactory
                .selectFrom(e)
                .where(e.feed.id.eq(feedId).and(e.emotionType.eq(emotionType)))
                .orderBy(e.createdAt.desc())
                .fetch();
    }
}
