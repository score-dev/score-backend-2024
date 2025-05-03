package com.score.backend.domain.rank;

import com.score.backend.domain.group.GroupEntity;
import com.score.backend.domain.group.UserGroup;
import com.score.backend.domain.rank.group.GroupRanker;
import com.score.backend.domain.rank.group.GroupRankerRepository;
import com.score.backend.domain.rank.group.GroupRanking;
import com.score.backend.domain.rank.group.GroupRankingRepository;
import com.score.backend.domain.rank.school.*;
import com.score.backend.domain.school.School;
import com.score.backend.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class RankingService {
    private final SchoolRankService schoolRankService;
    private final GroupRankerRepository groupRankerRepository;
    private final GroupRankingRepository groupRankingRepository;
    private final SchoolRankerRepository schoolRankerRepository;
    private final SchoolRankingRepository schoolRankingRepository;

    @Transactional(readOnly = true)
    public List<GroupRanker> findGroupRankersByUserId(Long userId) {
        return groupRankerRepository.findByUserId(userId);
    }

    public GroupRanking calculateWeeklyGroupRanking(GroupEntity group) {
        List<UserGroup> groupMates = group.getMembers();
        GroupRanking thisWeekGroupRanking = new GroupRanking(LocalDate.now().minusDays(7), LocalDate.now().minusDays(1), group);
        List<GroupRankingInfo> info = new ArrayList<>();
        for (UserGroup userGroup : groupMates) {
            info.add(new GroupRankingInfo(userGroup.getMember(), userGroup.getMember().getWeeklyLevelIncrement(), userGroup.getMember().getWeeklyCumulativeTime()));
        }

        if (info.size() >= 2) {
            info.sort((o1, o2) -> {
                if (o1.weeklyLevelIncrement == o2.weeklyLevelIncrement) {
                    return Double.compare(o2.weeklyExerciseTime, o1.weeklyExerciseTime);
                }
                return o2.weeklyLevelIncrement - o1.weeklyLevelIncrement;
            });
        }

        List<GroupRanker> thisWeekGroupRankers = new ArrayList<>();
        int rank = 1;
        for (int i = 0; i < info.size(); i++) {
            // 금주 순위 설정
            if (i > 0 && (info.get(i).weeklyLevelIncrement != info.get(i - 1).weeklyLevelIncrement
                    || info.get(i).weeklyExerciseTime != info.get(i - 1).weeklyExerciseTime)) {
                rank = i + 1;
            }
            info.get(i).rankNum = rank;

            GroupRanker lastWeekRankerInfo = groupRankerRepository.findLastWeekGroupRankerByGroupIdAndUserId(group.getGroupId(), info.get(i).user.getId());
            // 지난주와의 순위 비교 후 변동 추이 계산
            if (lastWeekRankerInfo != null) {
                info.get(i).changedDegree = (i + 1) - lastWeekRankerInfo.getRankNum();
            } else {
                info.get(i).changedDegree = 0;
            }
            GroupRanker gr = new GroupRanker(info.get(i).user, info.get(i).rankNum, info.get(i).changedDegree, info.get(i).weeklyLevelIncrement, info.get(i).weeklyExerciseTime);
            groupRankerRepository.save(gr);
            thisWeekGroupRankers.add(gr);
        }
        groupRankingRepository.save(thisWeekGroupRanking);
        // 연관 관계 설정
        for (GroupRanker groupRanker : thisWeekGroupRankers) {
            thisWeekGroupRanking.getGroupRankers().add(groupRanker);
            groupRanker.setBelongsTo(thisWeekGroupRanking);
        }
        return thisWeekGroupRanking;
    }

    public SchoolRanking calculateWeeklySchoolRanking(School school) {
        SchoolRanking thisWeekSchoolRanking = new SchoolRanking(LocalDate.now().minusDays(7), LocalDate.now().minusDays(1), school);
        List<SchoolRankingInfo> info = new ArrayList<>();

        // 이번주 순위 산출을 위해 참여율 및 총 운동 시간 연산
        for (GroupEntity group : school.getGroups()) {
            // 생성된 지 일주일이 되지 않은 그룹의 경우 순위 산정에서 제외
            if (group.getGroupCreatedAt().toLocalDate().isBefore(LocalDate.now().with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY)).minusWeeks(1))) {
                continue;
            }
            double weeklyCumulativeTime = 0.0;
            for (UserGroup userGroup : group.getMembers()) {
                weeklyCumulativeTime += userGroup.getMember().getWeeklyCumulativeTime();
            }
            info.add(new SchoolRankingInfo(group, getRatioOfParticipate(group), weeklyCumulativeTime));
        }

        // 이번주 순위에 따라 정렬
        if (info.size() >= 2) {
            info.sort((o1, o2) -> {
                if (o1.participateRatio == o2.participateRatio) {
                    return Double.compare(o2.totalExerciseTime, o1.totalExerciseTime);
                }
                return Double.compare(o2.participateRatio, o1.participateRatio);
            });
        }

        List<SchoolRanker> thisWeekSchoolRankers = new ArrayList<>();
        int rank = 1;
        for (int i = 0; i < info.size(); i++) {
            // 금주 순위 산정
            if (i > 0 && (info.get(i).participateRatio != info.get(i - 1).participateRatio
                    || info.get(i).totalExerciseTime != info.get(i - 1).totalExerciseTime)) {
                rank = i + 1;
            }
            info.get(i).rankNum = rank;

            SchoolRanker lastWeekRanker = schoolRankService.findLastWeekSchoolRankingByGroupId(info.get(i).group.getGroupId());
            if (lastWeekRanker != null) {
                info.get(i).changedDegree = (i + 1) - lastWeekRanker.getRankNum(); // 지난주와의 순위 변동
            } else {
                info.get(i).changedDegree = 0;
            }
            SchoolRanker sr = new SchoolRanker(info.get(i).group, info.get(i).participateRatio, info.get(i).totalExerciseTime, info.get(i).rankNum, info.get(i).changedDegree);
            schoolRankerRepository.save(sr);
            thisWeekSchoolRankers.add(sr);
        }
        schoolRankingRepository.save(thisWeekSchoolRanking);

        // 연관 관계 설정
        for (SchoolRanker schoolRanker : thisWeekSchoolRankers) {
            thisWeekSchoolRanking.getSchoolRankers().add(schoolRanker);
            schoolRanker.setBelongsTo(thisWeekSchoolRanking);
        }
        return thisWeekSchoolRanking;
    }

    // 그룹의 참여율 계산
    public double getRatioOfParticipate(GroupEntity group) {
        List<UserGroup> members = group.getMembers();
        if (!members.isEmpty()) {
            int sum = 0;
            for (UserGroup userGroup : members) {
                int exerciseCount = userGroup.getMember().getWeeklyExerciseCount();
                switch (exerciseCount) {
                    case 1: sum += 14; break;
                    case 2: sum += 28; break;
                    case 3: sum += 42; break;
                    case 4: sum += 56; break;
                    case 5: sum += 70; break;
                    case 6: sum += 84; break;
                    case 7: sum += 100; break;
                }
            }
            return  Math.round((double) sum / members.size());
        }
        return 0.0;
    }

    static class SchoolRankingInfo {
        private final GroupEntity group;
        private final double participateRatio;
        private final double totalExerciseTime;
        private int rankNum;
        private int changedDegree;
        private SchoolRankingInfo(GroupEntity group, double participateRatio, double totalExerciseTime) {
            this.group = group;
            this.participateRatio = participateRatio;
            this.totalExerciseTime = totalExerciseTime;
        }
    }

    static class GroupRankingInfo {
        private final User user;
        private final int weeklyLevelIncrement;
        private final double weeklyExerciseTime;
        private int rankNum;
        private int changedDegree;
        private GroupRankingInfo(User user, int weeklyLevelIncrement, double weeklyExerciseTime) {
            this.user = user;
            this.weeklyLevelIncrement = weeklyLevelIncrement;
            this.weeklyExerciseTime = weeklyExerciseTime;
        }

    }
}
