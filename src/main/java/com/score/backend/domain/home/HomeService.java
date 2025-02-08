package com.score.backend.domain.home;

import com.score.backend.domain.exercise.Exercise;
import com.score.backend.domain.exercise.ExerciseService;
import com.score.backend.domain.group.BatonService;
import com.score.backend.domain.group.GroupEntity;
import com.score.backend.domain.group.GroupService;
import com.score.backend.domain.group.UserGroup;
import com.score.backend.domain.user.User;
import com.score.backend.domain.user.UserService;
import com.score.backend.dtos.home.HomeGroupInfoResponse;
import com.score.backend.dtos.home.HomeNotExercisedUserResponse;
import com.score.backend.dtos.home.HomeResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class HomeService {
    private final UserService userService;
    private final ExerciseService  exerciseService;
    private final GroupService groupService;
    private final BatonService batonService;

    public HomeResponse getHomeInfo(Long userId) {
        User user = userService.findUserById(userId).get();
        List<UserGroup> userGroups = user.getUserGroups();
        List<Exercise> usersWeeklyExercises = exerciseService.getWeeklyExercises(userId);
        return new HomeResponse(user.getNickname(), user.getProfileImg(), user.getLevel(), user.getPoint(),
                cumulateExerciseTimeDayByDay(usersWeeklyExercises), user.getWeeklyCumulativeTime(), user.getWeeklyExerciseCount(), user.getConsecutiveDate(),
                userGroups.size(), getGroupInfos(userGroups.stream().map(UserGroup::getGroup).toList()));
    }

    private List<HomeGroupInfoResponse> getGroupInfos(List<GroupEntity> joinedGroups) {
        List<HomeGroupInfoResponse> groupInfos = new ArrayList<>();
        for (GroupEntity group : joinedGroups) {
            List<User> allUsersDidExerciseToday = groupService.findAllUsersDidExerciseToday(group.getGroupId());
            HomeGroupInfoResponse hgir = new HomeGroupInfoResponse(group.getGroupId(), group.getGroupName(), group.getMembers().size(),
                    getGroupExercisedMatesProfileUrl(allUsersDidExerciseToday), getHomeNotExercisedUserResponse(group.getGroupId()));
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

    private List<HomeNotExercisedUserResponse> getHomeNotExercisedUserResponse(Long groupId) {
        List<HomeNotExercisedUserResponse> hneur = new ArrayList<>();
        List<User> allMembersWhoDidNotExerciseToday = batonService.findAllMembersWhoDidNotExerciseToday(groupId);
        for (User user : allMembersWhoDidNotExerciseToday) {
            hneur.add(new HomeNotExercisedUserResponse(user.getId(), user.getNickname(), user.getProfileImg()));
        }
        return hneur;
    }
}
