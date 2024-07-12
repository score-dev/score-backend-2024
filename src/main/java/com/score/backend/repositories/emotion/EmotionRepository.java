package com.score.backend.repositories.emotion;

import com.score.backend.models.Emotion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmotionRepository extends JpaRepository<Emotion,Long>, EmotionRepositoryCustom {
}
