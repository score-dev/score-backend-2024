package com.score.backend.models.dtos;


import com.score.backend.models.User;
import lombok.Builder;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Time;

@Getter
public class UserDto {
    private final String nickname;
    private final int grade;
    private final int height;
    private final int weight;
    private final MultipartFile profileImg;
    private final Time goal;
    private final boolean marketing;
    private final boolean push;
    private final String loginKey;

    @Builder
    public UserDto(String nickname, int grade, int height, int weight, MultipartFile profileImg, Time goal, boolean marketing, boolean push, String loginKey) {
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
                .cumulativeTime(0.0)
                .level(1)
                .point(0)
                .consecutiveDate(0)
                .cumulativeDistance(0.0)
                .goal(goal)
                .marketing(marketing)
                .push(push)
                .loginKey(loginKey)
                .build();
    }
}
