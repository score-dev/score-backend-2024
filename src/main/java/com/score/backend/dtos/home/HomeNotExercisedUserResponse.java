package com.score.backend.dtos.home;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Schema(description = "오늘 운동을 쉰 유저의 정보 전달을 위한 DTO")
@Getter
public class HomeNotExercisedUserResponse {
    @Schema(description = "오늘 운동을 쉰 유저의 id")
    private Long userId;
    @Schema(description = "오늘 운동을 쉰 유저의 닉네임")
    private String nickname;
    @Schema(description = "오늘 운동을 쉰 유저의 프로필 이미지 url")
    private String profileImgUrl;
    @Schema(description = "오늘 해당 유저에게 바통을 찔렀는지 여부")
    private boolean canTurnOverBaton;

    public  HomeNotExercisedUserResponse(Long userId, String nickname, String profileImgUrl, boolean canTurnOverBaton) {
        this.userId = userId;
        this.nickname = nickname;
        this.profileImgUrl = profileImgUrl;
        this.canTurnOverBaton = canTurnOverBaton;
    }
}
