package com.score.backend.domain.rank.school;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SchoolRankService {
    private final SchoolRankerRepository schoolRankerRepository;

    public SchoolRanker findLastWeekSchoolRankingByGroupId(Long groupId) {
        return schoolRankerRepository.findLastWeekSchoolRankerByGroupId(groupId);
    }
}
