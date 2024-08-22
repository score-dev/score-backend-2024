package com.score.backend.services;

import com.score.backend.models.grouprank.GroupRanking;
import com.score.backend.repositories.GroupRankerRepository;
import com.score.backend.repositories.GroupRankingRepository;
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
