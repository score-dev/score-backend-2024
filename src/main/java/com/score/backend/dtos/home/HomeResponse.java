package com.score.backend.dtos.home;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
public class HomeResponse {
    private String nickname;
    private String profileImgUrl;
    private int level;
    private int point;
    private double weeklyExerciseTime;
    private int weeklyExerciseCount;
    private int consecutiveDate;
    private int numOfGroups;

    @Setter
    private List<HomeGroupInfoResponse> groupsInfo;

    public HomeResponse(String nickname, String profileImgUrl, int level, int point, double weeklyExerciseTime, int weeklyExerciseCount, int consecutiveDate, int numOfGroups) {
        this.nickname = nickname;
        this.profileImgUrl = profileImgUrl;
        this.level = level;
        this.point = point;
        this.weeklyExerciseTime = weeklyExerciseTime;
        this.weeklyExerciseCount = weeklyExerciseCount;
        this.consecutiveDate = consecutiveDate;
        this.numOfGroups = numOfGroups;
    }
}
