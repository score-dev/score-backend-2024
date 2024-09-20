package com.score.backend.models.dtos;

import lombok.Getter;

@Getter
public class UserResponseDto {
    private Long id;
    private String nickname;
    private String profileImgUrl;

    public UserResponseDto(Long id, String nickname, String profileImgUrl) {
        this.id = id;
        this.nickname = nickname;
        this.profileImgUrl = profileImgUrl;
    }
}
