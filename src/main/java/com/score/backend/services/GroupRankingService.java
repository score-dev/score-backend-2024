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
    public void initWeeklyLevelIncrement() {
        List<User> allUsers = userService.findAll();
        for (User user : allUsers) {
            user.initLevel();
        }
    }

    @Transactional(readOnly = true)
    public List<User> getWeeklyGroupRanking(Long groupId) {
        Group group = groupService.findById(groupId);
        List<User> groupMates = new ArrayList<>(group.getMembers().stream().toList());
        groupMates.sort((o1, o2) -> o2.getLastWeekLevelIncrement() - o1.getLastWeekLevelIncrement());
        return groupMates;
    }
}
