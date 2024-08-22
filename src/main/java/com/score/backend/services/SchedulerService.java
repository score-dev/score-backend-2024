package com.score.backend.services;

import com.score.backend.models.Group;
import com.score.backend.models.User;
import com.score.backend.models.grouprank.GroupRanker;
import com.score.backend.models.grouprank.GroupRanking;
import com.score.backend.repositories.GroupRankerRepository;
import com.score.backend.repositories.GroupRankingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
// 리팩토링 필요
public class SchedulerService {
    private final UserService userService;
    private final GroupService groupService;
    private final GroupRankingRepository groupRankingRepository;
    private final GroupRankerRepository groupRankerRepository;

    // 매일 0시에 실행
    // 전날 운동하지 않은 유저의 연속 운동 일수를 0으로 초기화
    @Scheduled(cron = "0 0 0 * * *")
    public void initEveryUsersConsecutiveDate() {
        LocalDateTime now = LocalDateTime.now().truncatedTo(ChronoUnit.DAYS);
        List<User> userList = userService.findAll();
        for (User user : userList) {
            if (user.getLastExerciseDateTime() == null) {
                continue;
            }
            LocalDateTime lastExerciseDate = user.getLastExerciseDateTime().truncatedTo(ChronoUnit.DAYS);
            if (ChronoUnit.DAYS.between(lastExerciseDate, now) > 1) {
                user.updateConsecutiveDate(false);
            }
        }
    }

    // 매주 월요일 0시에 실행
    @Scheduled(cron = "0 0 0 * * MON")
    public void initWeeklyExerciseStatus() {
        List<Group> allGroups = groupService.findAll();

        for (Group group : allGroups) {
            // 그룹 내 주간 랭킹 산정
            GroupRanking gr = calculateWeeklyGroupRanking(group.getGroupId());
            group.getGroupRankings().add(gr);
            // 이번 주 운동한 그룹원 수 0명으로 초기화
            group.initTodayExercisedCount();
            // 그룹 랭킹 1위인 유저에게 400포인트 지급
            if (!gr.getGroupRankers().isEmpty()) {
                gr.getGroupRankers().get(0).getUser().updatePoint(400);
            }
        }

        List<User> allUsers = userService.findAll();
        for (User user : allUsers) {
            user.initWeeklyExerciseStatus();
        }

        // 학교 그룹 산정 필요
    }

    private GroupRanking calculateWeeklyGroupRanking(Long groupId) {
        Group group = groupService.findById(groupId);
        GroupRanking lastWeekGroupRanking = group.getGroupRankings().get(group.getGroupRankings().size() - 1);
        List<User> groupMates = new ArrayList<>(group.getMembers().stream().toList());
        GroupRanking thisWeekGroupRanking = new GroupRanking(LocalDate.now().minusDays(7), LocalDate.now().minusDays(1), group);
        List<GroupRanker> thisWeekGroupRankers = new ArrayList<>();
        for (User user : groupMates) {
            GroupRanker gr = new GroupRanker(user, user.getWeeklyLevelIncrement(), user.getWeeklyCumulativeTime());
            thisWeekGroupRankers.add(gr);
        }

        if (thisWeekGroupRankers.size() >= 2) {
            thisWeekGroupRankers.sort((o1, o2) -> {
                if (o1.getWeeklyLevelIncrement() == o2.getWeeklyLevelIncrement()) {
                    return (int) (o2.getWeeklyExerciseTime() - o1.getWeeklyExerciseTime());
                }
                return o2.getWeeklyLevelIncrement() - o1.getWeeklyLevelIncrement();
            });
        }

        for (int i = 0; i < thisWeekGroupRankers.size(); i++) {
            thisWeekGroupRankers.get(i).setRank(i + 1); // 금주의 순위 설정
            // 지난주와의 순위 비교 후 변동 추이 계산
            if (lastWeekGroupRanking.getGroupRankers().contains(thisWeekGroupRankers.get(i))) {
                thisWeekGroupRankers.get(i).setChangedDegree(groupRankerRepository.findByGroupRankingIdAndUserId(lastWeekGroupRanking.getId(), thisWeekGroupRankers.get(i).getUser().getId()).getRank() - (i + 1));
            } else {
                thisWeekGroupRankers.get(i).setChangedDegree(0);
            }
            GroupRanker ranker = groupRankerRepository.save(thisWeekGroupRankers.get(i));
            ranker.setBelongingRanking(thisWeekGroupRanking);
            thisWeekGroupRanking.getGroupRankers().add(ranker);
        }
        return groupRankingRepository.save(thisWeekGroupRanking);
    }
}
