package com.score.backend.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class GroupMateTodaysExerciseDto {
    private Long userId;
    private String nickname;
    private String profileImgUrl;
    private LocalDateTime lastExerciseDateTime;
    private boolean didExerciseToday;
}
