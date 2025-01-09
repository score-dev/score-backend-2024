package com.score.backend.dtos.home;

import lombok.Getter;

@Getter
public class HomeNotExercisedUserResponse {
    private Long userId;
    private String nickname;
    private String profileImgUrl;
}
