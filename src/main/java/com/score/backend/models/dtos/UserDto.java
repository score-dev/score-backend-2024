package com.score.backend.models.dtos;

import com.score.backend.models.User;
import lombok.Builder;
import lombok.Getter;

import java.sql.Time;

@Getter
public class UserDto {
    private final String nickname;
    private final int grade;
    private final int height;
    private final int weight;
    private final String profileImg;
    private final Time goal;
    private final boolean marketing;
    private final boolean push;
    private final String loginKey;

    @Builder
    public UserDto(String nickname, int grade, int height, int weight, String profileImg, Time goal, boolean marketing, boolean push, String loginKey) {
        this.nickname = nickname;
        this.grade = grade;
        this.height = height;
        this.weight = weight;
        this.profileImg = profileImg;
        this.goal = goal;
        this.marketing = marketing;
        this.push = push;
        this.loginKey = loginKey;
    }

    public User toEntity() {
        return User.builder()
                .nickname(nickname)
                .grade(grade)
                .height(height)
                .weight(weight)
                .profileImg(profileImg)
                .goal(goal)
                .marketing(marketing)
                .push(push)
                .loginKey(loginKey)
                .build();
    }
}
