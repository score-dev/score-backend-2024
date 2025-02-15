package com.score.backend.domain.rank.group;

import com.score.backend.domain.user.User;
import com.score.backend.domain.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GroupRankService {
    private final UserService userService;
    private final GroupRankingRepository groupRankingRepository;
    private final GroupRankerRepository groupRankerRepository;

    public GroupRanking findRankingByGroupIdAndDate(Long groupId, LocalDate localDate) {
        return groupRankingRepository.findByGroupIdAndDate(groupId, localDate);
    }

    @Transactional
    public void handleWithdrawUsersRankingInfo(Long userId) {
        User dummyUser = userService.findDummyUser();
        List<GroupRanker> deletingUserRanks = groupRankerRepository.findByUserId(userId);
        deletingUserRanks.forEach(groupRanker -> groupRanker.setUser(dummyUser));
    }
}
