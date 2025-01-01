package com.score.backend.domain.rank.school;

import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface SchoolRankingRepositoryCustom {
    SchoolRanking findSchoolRankingByUserIdAndStartDate(Long userId, LocalDate startDate);
}
