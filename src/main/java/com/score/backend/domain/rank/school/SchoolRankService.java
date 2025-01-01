package com.score.backend.domain.rank.school;

import com.score.backend.domain.group.GroupEntity;
import com.score.backend.dtos.schoolrank.SchoolRankerResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SchoolRankService {
    private final SchoolRankingRepository schoolRankingRepository;
    private final SchoolRankerRepository schoolRankerRepository;

    public SchoolRanker findLastWeekSchoolRankingByGroupId(Long groupId) {
        return schoolRankerRepository.findLastWeekSchoolRankerByGroupId(groupId);
    }

    public List<SchoolRankerResponse> findAllSchoolRankingByUserId(Long userId, LocalDate startDate) {
        SchoolRanking schoolRanking = schoolRankingRepository.findSchoolRankingByUserIdAndStartDate(userId, startDate);
        List<SchoolRankerResponse> rankers = new ArrayList<>();
        for (SchoolRanker ranker : schoolRanking.getSchoolRankers()) {
            GroupEntity group = ranker.getGroup();
            rankers.add(new SchoolRankerResponse(
                    group.getGroupId(),
                    group.getGroupName(),
                    group.getGroupImg(),
                    group.isPrivate(),
                    group.getUserLimit(),
                    group.getMembers().size(),
                    ranker.getRankNum(),
                    ranker.getChangedAmount(),
                    ranker.getParticipateRatio(),
                    ranker.getTotalExerciseTime()));

        }
        return rankers;
    }

    public List<SchoolRankerResponse> findMyGroupRankingByUserId(Long userId, LocalDate startDate) {
        List<SchoolRanker> myGroups = schoolRankerRepository.findSchoolRankersByUserIdAndStartDate(userId, startDate);
        List<SchoolRankerResponse> rankers = new ArrayList<>();
        for (SchoolRanker ranker : myGroups) {
            GroupEntity group = ranker.getGroup();
            rankers.add(new SchoolRankerResponse(
                    group.getGroupId(),
                    group.getGroupName(),
                    group.getGroupImg(),
                    ranker.getRankNum(),
                    ranker.getParticipateRatio(),
                    ranker.getTotalExerciseTime()
            ));
        }
        return rankers;
    }
}
