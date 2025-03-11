package com.score.backend.domain.home;

import com.score.backend.domain.exercise.Exercise;
import com.score.backend.domain.exercise.ExerciseService;
import com.score.backend.domain.group.BatonService;
import com.score.backend.domain.group.GroupEntity;
import com.score.backend.domain.group.GroupService;
import com.score.backend.domain.group.UserGroup;
import com.score.backend.domain.report.userreport.UserReportService;
import com.score.backend.domain.user.User;
import com.score.backend.domain.user.UserService;
import com.score.backend.dtos.home.HomeGroupInfoResponse;
import com.score.backend.dtos.home.HomeNotExercisedUserResponse;
import com.score.backend.dtos.home.HomeResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
public class HomeService {
    private final UserReportService userReportService;
    private final UserService userService;
    private final ExerciseService  exerciseService;
    private final GroupService groupService;
    private final BatonService batonService;

    public HomeResponse getHomeInfo(Long userId) {
        User user = userService.findUserById(userId);
        List<UserGroup> userGroups = user.getUserGroups();
        userGroups.sort(Comparator.comparing(UserGroup::getJoinedAt).reversed());
        if (userGroups.size() > 3) {
            userGroups = userGroups.subList(0, 3);
        }
        List<Exercise> usersWeeklyExercises = exerciseService.getWeeklyExercises(userId);
        return new HomeResponse(userReportService.getUserReportCount(userId) >= 5,
                user.getNickname(), user.getProfileImg(), user.getLevel(), user.getPoint(),
                cumulateExerciseTimeDayByDay(usersWeeklyExercises), user.getWeeklyCumulativeTime(), user.getWeeklyExerciseCount(), user.getConsecutiveDate(),
                userGroups.size(), getGroupInfos(user, userGroups.stream().map(UserGroup::getGroup).toList()));
    }

    private List<HomeGroupInfoResponse> getGroupInfos(User agent, List<GroupEntity> joinedGroups) {
        List<HomeGroupInfoResponse> groupInfos = new ArrayList<>();
        for (GroupEntity group : joinedGroups) {
            HomeGroupInfoResponse hgir = new HomeGroupInfoResponse(group.getGroupId(), group.getGroupName(), group.getMembers().size(),
                    getGroupExercisedMatesProfileUrl(groupService.findAllUsersDidExerciseToday(group.getGroupId())),
                    getHomeNotExercisedUserResponse(agent, group.getGroupId()));
            groupInfos.add(hgir);
        }
        return groupInfos;
    }

    private List<String> getGroupExercisedMatesProfileUrl(List<User> allUsersDidExerciseToday) {
        List<String> groupExercisedMatesProfileUrl = new ArrayList<>();
        allUsersDidExerciseToday.forEach(u -> {
            groupExercisedMatesProfileUrl.add(u.getProfileImg());
        });
        return groupExercisedMatesProfileUrl;
    }

    private List<Double> cumulateExerciseTimeDayByDay(List<Exercise> usersWeeklyExercises) {
        Double[] exerciseTimes = new Double[7];
        for (Exercise exercise : usersWeeklyExercises) {
            double duration = exerciseService.calculateExerciseDuration(exercise.getStartedAt(), exercise.getCompletedAt());
            exerciseTimes[exercise.getStartedAt().getDayOfWeek().getValue() - 1] = duration;
        }
        return Arrays.asList(exerciseTimes);
    }

    private List<HomeNotExercisedUserResponse> getHomeNotExercisedUserResponse(User agent, Long groupId) {
        List<HomeNotExercisedUserResponse> hneur = new ArrayList<>();
        List<User> randomMembersWhoDidNotExerciseToday = getRandomThreeUnexercisedUsers(agent, batonService.findAllMembersWhoDidNotExerciseToday(groupId));
        for (User user : randomMembersWhoDidNotExerciseToday) {
            hneur.add(new HomeNotExercisedUserResponse(user.getId(), user.getNickname(), user.getProfileImg(), batonService.canTurnOverBaton(agent.getId(), user.getId())));
        }
        return hneur;
    }

    // 오늘 3분 이상 운동하지 않은 그룹 메이트 중 자기 자신을 제외한 랜덤 3명을 선택
    private List<User> getRandomThreeUnexercisedUsers(User agent, List<User> unexercisedUsers) {
        unexercisedUsers.remove(agent); // 자기 자신은 목록에서 제외
        if (unexercisedUsers.size() <= 3) {
            return unexercisedUsers;
        }

        Random random = new Random();
        return IntStream.generate(() -> random.nextInt(unexercisedUsers.size()))
                .distinct()
                .limit(3)
                .mapToObj(unexercisedUsers::get)
                .collect(Collectors.toList());
    }
}
