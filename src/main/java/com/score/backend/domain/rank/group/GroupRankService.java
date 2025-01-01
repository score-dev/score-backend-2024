package com.score.backend.domain.rank.group;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class GroupRankService {
    private final GroupRankingRepository groupRankingRepository;
    private final GroupRankerRepository groupRankerRepository;

    public GroupRanking findRankingByGroupIdAndDate(Long groupId, LocalDate localDate) {
        return groupRankingRepository.findByGroupIdAndDate(groupId, localDate);
    }

}
