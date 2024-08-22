package com.score.backend.services;

import com.score.backend.models.Group;
import com.score.backend.models.User;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional
public class GroupRankingService {
    private final GroupService groupService;
    private final UserService userService;

    // 매주 월요일 0시에 모든 유저들의 금주 레벨 상승 횟수를 지난주 레벨 상승 횟수로 이동시켜 저장, 금주의 레벨 상승 횟수는 0으로 초기화
    @Scheduled(cron = "0 0 0 * * MON")
    public void initWeeklyExerciseStatus() {
        List<User> allUsers = userService.findAll();
        for (User user : allUsers) {
            user.initWeeklyExerciseStatus();
        }
        List<Group> allGroups = groupService.findAll();
        // 그룹 랭킹 1위인 유저에게 400포인트 지급
        for (Group group : allGroups) {
            List<User> userRanking = getWeeklyGroupRanking(group.getGroupId());
            if (!userRanking.isEmpty()) {
                userRanking.get(0).updatePoint(400);
            }
        }
    }

    @Transactional(readOnly = true)
    public List<User> getWeeklyGroupRanking(Long groupId) {
        Group group = groupService.findById(groupId);
        List<User> groupMates = new ArrayList<>(group.getMembers().stream().toList());
        if (groupMates.size() >= 2) {
            groupMates.sort((o1, o2) -> {
                if (o1.getLastWeekLevelIncrement() == o2.getLastWeekLevelIncrement()) {
                    return (int) (o2.getLastWeekCumulativeTime() - o1.getLastWeekCumulativeTime());
                }
                return o2.getLastWeekLevelIncrement() - o1.getLastWeekLevelIncrement();
            });
        }
        return groupMates;
    }
}
