package com.score.backend;

import com.google.firebase.messaging.FirebaseMessagingException;
import com.score.backend.domain.group.GroupService;
import com.score.backend.domain.group.UserGroup;
import com.score.backend.domain.notification.NotificationService;
import com.score.backend.domain.rank.RankingService;
import com.score.backend.domain.rank.school.SchoolRanking;
import com.score.backend.domain.school.School;
import com.score.backend.domain.school.SchoolService;
import com.score.backend.domain.user.UserService;
import com.score.backend.domain.group.GroupEntity;
import com.score.backend.domain.user.User;
import com.score.backend.dtos.FcmMessageRequest;
import com.score.backend.domain.rank.group.GroupRanking;

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

        List<User> allUsers = userService.findAll();
        for (User user : allUsers) {
            user.initWeeklyExerciseStatus();
        }

        List<School> allSchools = schoolService.findAll();
        for (School school : allSchools) {
            school.getSchoolRankings().add(calculateSchoolRanking(school));
        }
    }

    private GroupRanking calculateGroupRanking(GroupEntity group) throws FirebaseMessagingException {
        // 그룹 내 주간 랭킹 산정
        GroupRanking gr = rankingService.calculateWeeklyGroupRanking(group);
        if (!gr.getGroupRankers().isEmpty() && gr.getGroupRankers().size() > 1) {
            // 그룹 랭킹 1위인 유저에게 400포인트 지급
            gr.getGroupRankers().get(0).getUser().updatePoint(400);
            // 그룹 랭킹 1위인 유저에게 알림 발송
            if (!gr.getGroupRankers().get(0).getUser().getLoginKey().equals("string")) {
                FcmMessageRequest message = new FcmMessageRequest(
                        gr.getGroupRankers().get(0).getUser().getId(),
                        gr.getGroup().getGroupName() + " 그룹에서 1위를 달성했어요!",
                        "축하합니다\uD83C\uDF89 " + gr.getGroupRankers().get(0).getUser().getNickname() +  "님이 이번주 1등이에요! 1등이 된 기념으로 스코어에서 400pt를 쏩니다\uD83E\uDD73"
                );
                notificationService.sendMessage(message);
                notificationService.saveNotification(message); // 알림 저장
            }
        }
        return gr;
    }

    private SchoolRanking calculateSchoolRanking(School school) throws FirebaseMessagingException {
        SchoolRanking sr = rankingService.calculateWeeklySchoolRanking(school);
        if (!sr.getSchoolRankers().isEmpty() && sr.getSchoolRankers().size() > 1) {
            List<UserGroup> winningGroupMembers = sr.getSchoolRankers().get(0).getGroup().getMembers();
            for (UserGroup winningGroupMember : winningGroupMembers) {
                winningGroupMember.getMember().updatePoint(200);
                if (!winningGroupMember.getMember().getLoginKey().equals("string")) {
                    FcmMessageRequest message = new FcmMessageRequest(
                            winningGroupMember.getId(),
                            sr.getSchoolRankers().get(0).getGroup().getGroupName() + " 그룹이 " + school.getSchoolName() + "에서 1위를 달성했어요!",
                            "축하합니다\uD83C\uDF89 " + winningGroupMember.getMember().getNickname() +  "님이 속한 그룹이 이번주 1위예요! 1등이 된 기념으로 스코어에서 그룹 메이트 모두에게 200pt를 쏩니다\uD83E\uDD73"
                    );
                    notificationService.sendMessage(message);
                    notificationService.saveNotification(message);
                }
            }
        }

        return sr;
    }

    @Scheduled(fixedRate = 60000) // 1분마다 실행
    public void checkUsersGoalExercisingTime() {
        LocalTime currentTime = LocalTime.now();
        List<User> usersToNotify = userService.findUsersByGoal(currentTime);
        for (User user : usersToNotify) {
            alertExerciseTimeAndSaveNotification(user);
        }
    }

    @Async
    protected void alertExerciseTimeAndSaveNotification(User user) {
        try {
            if (!user.getLoginKey().equals("string")) {
                FcmMessageRequest message = new FcmMessageRequest(user.getId(),
                        "목표한 운동 시간이 되었어요!", "오늘도 스코어와 함께 운동을 시작해요\uD83D\uDE4C\uD83C\uDFFB");
                notificationService.sendMessage(message);
                notificationService.saveNotification(message);
            }
        } catch(FirebaseMessagingException e) {
            e.printStackTrace();
        }

    }
}