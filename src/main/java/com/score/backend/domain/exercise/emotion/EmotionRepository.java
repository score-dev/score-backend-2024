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
    @Query(value = "DELETE FROM emotion " +
            "WHERE user_id = :agentId " +
            "AND exercise_id = :feedId " +
            "AND emotion_type = :type",
            nativeQuery = true)
    int deleteEmotionNative(@Param("agentId") Long agentId,
                            @Param("feedId") Long feedId,
                            @Param("type") String type);

    @Modifying
    @Query(value = "INSERT INTO emotion (user_id, exercise_id, emotion_type) " +
            "SELECT :agentId, :feedId, :type " +
            "WHERE ROW_COUNT() = 0",
            nativeQuery = true)
    void insertIfNotDeleted(@Param("agentId") Long agentId,
                           @Param("feedId") Long feedId,
                           @Param("type") String type);
}
