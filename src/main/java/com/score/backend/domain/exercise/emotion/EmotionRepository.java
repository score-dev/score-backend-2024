package com.score.backend.domain.exercise.emotion;

import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmotionRepository extends JpaRepository<Emotion,Long>, EmotionRepositoryCustom {
    @Query("select e from Emotion e where e.feed.id = :feedId")
    Optional<List<Emotion>> findAllEmotionsByFeedId(@Param("feedId") Long feedId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select e from Emotion e where e.agent.id = :userId and e.feed.id = :feedId and e.emotionType = :type")
    Optional<Emotion> findUsersEmotionByFeedIdAndEmotionType(@Param("feedId") Long feedId, @Param("userId") Long userId, @Param("type") EmotionType type);
}
