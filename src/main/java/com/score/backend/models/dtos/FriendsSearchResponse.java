package com.score.backend.models.dtos;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "친구 검색 결과 응답을 위한 DTO")
public class FriendsSearchResponse {
    @Schema(description = "검색된 유저의 고유 id 값")
    private Long id;
    @Schema(description = "검섹된 유저의 닉네임(unique)", maxLength = 10, example = "김승주")
    private String nickname;
    @Schema(description = "검색된 유저의 프로필 이미지 URL")
    private String profileImgUrl;

    public FriendsSearchResponse(Long id, String nickname, String profileImgUrl) {
        this.id = id;
        this.nickname = nickname;
        this.profileImgUrl = profileImgUrl;
    }
}
