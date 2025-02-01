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

    public  HomeNotExercisedUserResponse(Long userId, String nickname, String profileImgUrl) {
        this.userId = userId;
        this.nickname = nickname;
        this.profileImgUrl = profileImgUrl;
    }
}
