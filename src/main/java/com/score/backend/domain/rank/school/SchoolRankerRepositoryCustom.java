package com.score.backend.domain.rank.school;

import org.springframework.stereotype.Repository;

@Repository
public interface SchoolRankerRepositoryCustom {
    SchoolRanker findLastWeekSchoolRankerByGroupId(Long groupId);
}
