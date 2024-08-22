package com.score.backend.repositories;

import com.score.backend.models.grouprank.GroupRanking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GroupRankingRepository extends JpaRepository<GroupRanking, Long> {
}
