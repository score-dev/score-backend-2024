package com.score.backend.domain.exercise.emotion;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmotionRepository extends JpaRepository<Emotion,Long>, EmotionRepositoryCustom {
    @Query("select e from Emotion e where e.feed.id = :feedId")
    Optional<List<Emotion>> findAllEmotionsByFeedId(@Param("feedId") Long feedId);

    @Modifying
    @Query("DELETE FROM Emotion e WHERE e.agent.id = :agentId AND e.feed.id = :feedId AND e.emotionType = :type")
    int deleteEmotion(@Param("agentId") Long agentId,
                       @Param("feedId") Long feedId,
                       @Param("type") EmotionType type);

    @Modifying
    @Query(
                value = "INSERT INTO emotion (user_id, exercise_id, emotion_type) " +
                        "VALUES (:agentId, :feedId, :type) " +
                        "ON DUPLICATE KEY UPDATE emotion_type = :type",
                nativeQuery = true
    )
    void insertEmotion(@Param("agentId") Long agentId, @Param("feedId") Long feedId, @Param("type") String type);
}

