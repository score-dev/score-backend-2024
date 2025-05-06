package com.score.backend;

import com.google.firebase.messaging.FirebaseMessagingException;
import com.score.backend.domain.group.GroupService;
import com.score.backend.domain.group.UserGroup;
import com.score.backend.domain.notification.NotificationService;
import com.score.backend.domain.notification.NotificationType;
import com.score.backend.domain.rank.RankingService;
import com.score.backend.domain.rank.group.GroupRanker;
import com.score.backend.domain.rank.school.SchoolRanker;
import com.score.backend.domain.rank.school.SchoolRanking;
import com.score.backend.domain.school.School;
import com.score.backend.domain.school.SchoolService;
import com.score.backend.domain.user.UserService;
import com.score.backend.domain.group.GroupEntity;
import com.score.backend.domain.user.User;
import com.score.backend.domain.rank.group.GroupRanking;

import com.score.backend.dtos.NotificationDto;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class SchedulerService {
    private final UserService userService;
    private final GroupService groupService;
    private final RankingService rankingService;
    private final NotificationService notificationService;
    private final SchoolService schoolService;

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
    public void executeWeeklyScheduledTask() throws FirebaseMessagingException {
        List<GroupEntity> allGroups = groupService.findAll();
        for (GroupEntity group : allGroups) {
            group.getGroupRankings().add(calculateGroupRanking(group));
        }

        List<School> allSchools = schoolService.findAll();
        for (School school : allSchools) {
            if (!school.getGroups().isEmpty()) {
                school.getSchoolRankings().add(calculateSchoolRanking(school));
            }
        }

        List<User> allUsers = userService.findAll();
        for (User user : allUsers) {
            user.initWeeklyExerciseStatus();
        }
    }

    private GroupRanking calculateGroupRanking(GroupEntity group) {
        // 그룹 내 주간 랭킹 산정
        GroupRanking gr = rankingService.calculateWeeklyGroupRanking(group);
        if (!gr.getGroupRankers().isEmpty() && gr.getGroupRankers().size() > 1) {
            for (int i = 0; i < gr.getGroupRankers().size(); i++) {
                GroupRanker currGroupRanker = gr.getGroupRankers().get(i);
                if (currGroupRanker.getRankNum() > 1) {
                    break;
                }
                // 그룹 랭킹 1위인 유저에게 400포인트 지급
                currGroupRanker.getUser().updatePoint(400);
                // 그룹 랭킹 1위인 유저에게 알림 발송

                NotificationDto dto = NotificationDto.builder()
                        .receiver(currGroupRanker.getUser())
                        .relatedGroup(group)
                        .type(NotificationType.GROUP_RANKING)
                        .build();

                notificationService.sendAndSaveNotification(dto);
            }
        }
        return gr;
    }

    private SchoolRanking calculateSchoolRanking(School school) {
        SchoolRanking sr = rankingService.calculateWeeklySchoolRanking(school);
        if (!sr.getSchoolRankers().isEmpty() && sr.getSchoolRankers().size() > 1) {
            for (int i = 0; i < sr.getSchoolRankers().size(); i++) {
                SchoolRanker currSchoolRanker = sr.getSchoolRankers().get(i);
                if (currSchoolRanker.getRankNum() > 1) {
                    break;
                }

                // 1위 학교 그룹 내 모든 유저에게 800포인트 지급 및 알림 발송
                List<UserGroup> winningGroupMembers = currSchoolRanker.getGroup().getMembers();
                for (UserGroup winningGroupMember : winningGroupMembers) {
                    winningGroupMember.getMember().updatePoint(800);
                    NotificationDto dto = NotificationDto.builder()
                            .receiver(winningGroupMember.getMember())
                            .relatedGroup(winningGroupMember.getGroup())
                            .type(NotificationType.SCHOOL_RANKING)
                            .build();
                    notificationService.sendAndSaveNotification(dto);
                }
            }
        }

        return sr;
    }

    @Scheduled(cron = "0 * * * * *") // 매 분 실행
    public void checkUsersGoalExercisingTime() {
        LocalTime currentTime = LocalTime.now().withSecond(0).withNano(0);
        List<User> usersToNotify = userService.findUsersByGoal(currentTime);
        for (User user : usersToNotify) {
            alertExerciseTimeAndSaveNotification(user);
        }
    }

    @Async
    protected void alertExerciseTimeAndSaveNotification(User user)  {
        if (user.isExercisingTime()) {
            NotificationDto dto = NotificationDto.builder()
                    .receiver(user)
                    .type(NotificationType.GOAL)
                    .build();
            notificationService.sendAndSaveNotification(dto);
        }
    }
}
