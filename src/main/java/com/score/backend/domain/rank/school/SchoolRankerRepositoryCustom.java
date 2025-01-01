package com.score.backend.domain.rank.school;

import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface SchoolRankerRepositoryCustom {
    SchoolRanker findLastWeekSchoolRankerByGroupId(Long groupId);
    List<SchoolRanker> findSchoolRankersByUserIdAndStartDate(Long userId, LocalDate startDate);
}
