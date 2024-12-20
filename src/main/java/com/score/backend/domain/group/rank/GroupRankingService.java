package com.score.backend.domain.group.rank;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class GroupRankingService {
    private final GroupRankingRepository groupRankingRepository;
    private final GroupRankerRepository groupRankerRepository;

    public GroupRanking findRankingByGroupIdAndDate(Long groupId, LocalDate localDate) {
        return groupRankingRepository.findByGroupIdAndDate(groupId, localDate);
    }

}
