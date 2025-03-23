package com.score.backend.dtos.home;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.util.List;

@Schema(description = "홈 화면에 표시될 유저가 가입해 있는 그룹 정보를 전달하기 위한 DTO")
@Getter
public class HomeGroupInfoResponse {
    @Schema(description = "그룹의 id")
    private Long groupId;
    @Schema(description = "그룹명")
    private String groupName;
    @Schema(description = "그룹 내 가입된 유저의 수")
    private int numOfMembers;
    @Schema(description = "그룹 내 모든 멤버들의 프로필 이미지 url")
    private List<String> wholeMatesImgUrl;
    @Schema(description = "오늘 운동한 그룹 멤버들의 프로필 이미지 url")
    private List<String> todayExercisedMatesImgUrl;
    @Schema(description = "오늘 운동하지 않은 유저들에 대한 정보를 전달하기 위한 DTO")
    private List<HomeNotExercisedUserResponse> notExercisedUsers;

    public HomeGroupInfoResponse(Long groupId, String groupName, int numOfMembers,  List<String> todayExercisedMatesImgUrl, List<String> wholeMatesImgUrl, List<HomeNotExercisedUserResponse> notExercisedUsers) {
        this.groupId = groupId;
        this.groupName = groupName;
        this.numOfMembers = numOfMembers;
        this.wholeMatesImgUrl = wholeMatesImgUrl;
        this.todayExercisedMatesImgUrl = todayExercisedMatesImgUrl;
        this.notExercisedUsers = notExercisedUsers;
    }
}
