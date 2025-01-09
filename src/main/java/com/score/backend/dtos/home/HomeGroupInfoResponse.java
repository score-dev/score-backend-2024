package com.score.backend.dtos.home;

import lombok.Getter;

import java.util.List;

@Getter
public class HomeGroupInfoResponse {
    private Long groupId;
    private String groupName;
    private int numOfMembers;
    private int NumOfExercisedToday;
    private List<String> todayExercisedMatesImgUrl;
    private List<HomeNotExercisedUserResponse> notExercisedUsers;
}
