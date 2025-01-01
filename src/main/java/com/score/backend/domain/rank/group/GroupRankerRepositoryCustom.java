package com.score.backend.domain.rank.group;

import org.springframework.stereotype.Repository;

@Repository
public interface GroupRankerRepositoryCustom {
    GroupRanker findLastWeekGroupRankerByGroupIdAndUserId(Long groupId, Long userId);
}
