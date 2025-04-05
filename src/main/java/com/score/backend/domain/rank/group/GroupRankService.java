package com.score.backend.domain.rank.group;

import com.score.backend.domain.user.User;
import com.score.backend.domain.user.UserService;
import com.score.backend.dtos.GroupRankerResponse;
import com.score.backend.dtos.GroupRankingResponse;
import com.score.backend.exceptions.ExceptionType;
import com.score.backend.exceptions.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GroupRankService {
    private final UserService userService;
    private final GroupRankingRepository groupRankingRepository;
    private final GroupRankerRepository groupRankerRepository;

    private GroupRanking findRankingByGroupIdAndDate(Long groupId, LocalDate localDate) {
        return groupRankingRepository.findByGroupIdAndDate(groupId, localDate);
    }

    public GroupRankingResponse getGroupRankingResponse(Long groupId, LocalDate localDate) {
        GroupRanking groupRanking = findRankingByGroupIdAndDate(groupId, localDate);
        if (groupRanking == null) {
            throw new NotFoundException(ExceptionType.RANKING_NOT_FOUND);
        }
        List<GroupRankerResponse> groupRankerResponses = new ArrayList<>();
        List<GroupRanker> rankers = groupRanking.getGroupRankers();
        for (GroupRanker ranker : rankers) {
            groupRankerResponses.add(new GroupRankerResponse(ranker));
        }
        return new GroupRankingResponse(groupRanking, groupRankerResponses);
    }

    @Transactional
    public void handleWithdrawUsersRankingInfo(Long userId) {
        User dummyUser = userService.findDummyUser();
        List<GroupRanker> deletingUserRanks = groupRankerRepository.findByUserId(userId);
        deletingUserRanks.forEach(groupRanker -> groupRanker.setUser(dummyUser));
    }
}
