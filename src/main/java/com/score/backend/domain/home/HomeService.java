package com.score.backend.domain.home;

import com.score.backend.domain.exercise.Exercise;
import com.score.backend.domain.exercise.ExerciseService;
import com.score.backend.domain.group.BatonService;
import com.score.backend.domain.group.GroupEntity;
import com.score.backend.domain.group.UserGroup;
import com.score.backend.domain.report.userreport.UserReportService;
import com.score.backend.domain.user.User;
import com.score.backend.domain.user.UserService;
import com.score.backend.dtos.GroupMateTodaysExerciseDto;
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
    private final UserService userService;
    private final UserReportService userReportService;
    private final ExerciseService  exerciseService;
    private final BatonService batonService;

    public HomeResponse getHomeInfo(User user, List<Exercise> usersWeeklyExercises) {
        List<UserGroup> userGroups = user.getUserGroups();
        userGroups.sort(Comparator.comparing(UserGroup::getJoinedAt).reversed());
        if (userGroups.size() > 3) {
            userGroups = userGroups.subList(0, 3);
        }
        return new HomeResponse(userReportService.getUserReportCount(user) >= 5,
                user.getNickname(), user.getProfileImg(), user.getLevel(), user.getPoint(),
                cumulateExerciseTimeDayByDay(usersWeeklyExercises), user.getWeeklyCumulativeTime(), user.getWeeklyExerciseCount(), user.getConsecutiveDate(),
                user.getUserGroups().size(), getGroupInfos(user, userGroups.stream().map(UserGroup::getGroup).toList()));
    }

    private List<HomeGroupInfoResponse> getGroupInfos(User agent, List<GroupEntity> joinedGroups) {
        List<HomeGroupInfoResponse> groupInfos = new ArrayList<>();
        for (GroupEntity group : joinedGroups) {
            List<GroupMateTodaysExerciseDto> groupMateTodaysExerciseDtos = userService.findGroupMateTodaysExerciseByGroupId(group.getGroupId());
            Map<Boolean, List<GroupMateTodaysExerciseDto>> classified = classifyGroupMates(groupMateTodaysExerciseDtos);
            HomeGroupInfoResponse hgir = new HomeGroupInfoResponse(group.getGroupId(), group.getGroupName(), group.getMembers().size(),
                    getGroupExercisedMatesProfileUrl(classified.get(true)),
                    group.getMembers().stream().map(UserGroup::getMember).map(User::getProfileImg).toList(),
                    getHomeNotExercisedUserResponse(agent, classified.get(false)));
            groupInfos.add(hgir);
        }
        return groupInfos;
    }

    private Map<Boolean, List<GroupMateTodaysExerciseDto>> classifyGroupMates(List<GroupMateTodaysExerciseDto> dtos) {
        return dtos.stream().collect(
                Collectors.partitioningBy(GroupMateTodaysExerciseDto::isDidExerciseToday)
        );
    }

    private List<String> getGroupExercisedMatesProfileUrl(List<GroupMateTodaysExerciseDto> allUsersDidExerciseToday) {
        allUsersDidExerciseToday.sort(Comparator.comparing(GroupMateTodaysExerciseDto::getLastExerciseDateTime));
        return allUsersDidExerciseToday.stream().map(GroupMateTodaysExerciseDto::getProfileImgUrl).toList();
    }

    private List<Double> cumulateExerciseTimeDayByDay(List<Exercise> usersWeeklyExercises) {
        Double[] exerciseTimes = new Double[7];
        for (Exercise exercise : usersWeeklyExercises) {
            double duration = exerciseService.calculateExerciseDuration(exercise.getStartedAt(), exercise.getCompletedAt());
            exerciseTimes[exercise.getStartedAt().getDayOfWeek().getValue() - 1] = duration;
        }
        return Arrays.asList(exerciseTimes);
    }

    private List<HomeNotExercisedUserResponse> getHomeNotExercisedUserResponse(User agent, List<GroupMateTodaysExerciseDto> didNotExercisedToday) {
        List<HomeNotExercisedUserResponse> hneur = new ArrayList<>();
        List<GroupMateTodaysExerciseDto> randomMembersWhoDidNotExerciseToday = getRandomThreeUnexercisedUsers(agent.getId(), didNotExercisedToday);
        for (GroupMateTodaysExerciseDto dto : randomMembersWhoDidNotExerciseToday) {
            hneur.add(new HomeNotExercisedUserResponse(dto.getUserId(), dto.getNickname(), dto.getProfileImgUrl(), batonService.canTurnOverBaton(agent, userService.findUserById(dto.getUserId()))));
        }
        return hneur;
    }

    // 오늘 3분 이상 운동하지 않은 그룹 메이트 중 자기 자신을 제외한 랜덤 3명을 선택
    private List<GroupMateTodaysExerciseDto> getRandomThreeUnexercisedUsers(Long agentId, List<GroupMateTodaysExerciseDto> didNotExercisedToday) {
        // 자기 자신 제외
        List<GroupMateTodaysExerciseDto> exceptAgent = didNotExercisedToday.stream().filter(dto -> !dto.getUserId().equals(agentId)).collect(Collectors.toList());
        if (exceptAgent.size() <= 3) {
            return exceptAgent;
        }

        Random random = new Random();
        return IntStream.generate(() -> random.nextInt(exceptAgent.size()))
                .distinct()
                .limit(3)
                .mapToObj(didNotExercisedToday::get)
                .collect(Collectors.toList());
    }
}
