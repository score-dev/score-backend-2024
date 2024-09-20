package com.score.backend.models.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Schema(description = "오늘 운동을 쉰 유저의 정보 전달을 위한 DTO")
@Getter
public class BatonStatusDto {
    @Schema(description = "유저의 id")
    private Long userId;
    @Schema(description = "유저의 닉네임")
    private String nickname;
    @Schema(description = "유저의 프로필 사진 URL")
    private String profileImageUrl;
    @Schema(description = "dto를 요청한 유저가 이 유저에게 이미 바통을 찔렀는지 여부")
    private boolean canTurnOverBaton;

    public BatonStatusDto(Long userId, String nickname, String profileImageUrl, boolean canTurnOverBaton) {
        this.userId = userId;
        this.nickname = nickname;
        this.profileImageUrl = profileImageUrl;
        this.canTurnOverBaton = canTurnOverBaton;
    }
}
