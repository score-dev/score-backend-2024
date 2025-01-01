package com.score.backend.domain.rank.school;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SchoolRankingRepository extends JpaRepository<SchoolRanking, Long> {
}
