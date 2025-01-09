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
    private List<Double> weeklyExerciseTime;
    private double weeklyExerciseDistance;
    private double weeklyReducedKcal;
    private int numOfGroups;

    @Setter
    private List<HomeGroupInfoResponse> groupsInfo;

    public HomeResponse(String nickname, String profileImgUrl, int level, int point, List<Double> weeklyExerciseTime, double weeklyExerciseDistance, double weeklyReducedKcal, int numOfGroups) {
        this.nickname = nickname;
        this.profileImgUrl = profileImgUrl;
        this.level = level;
        this.point = point;
        this.weeklyExerciseTime = weeklyExerciseTime;
        this.weeklyExerciseDistance = weeklyExerciseDistance;
        this.weeklyReducedKcal = weeklyReducedKcal;
        this.numOfGroups = numOfGroups;
    }
}
