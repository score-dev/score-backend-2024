package com.score.backend.controllers;

import com.score.backend.models.User;
import com.score.backend.models.dtos.WalkingDto;
import com.score.backend.models.exercise.Exercise;
import com.score.backend.models.exercise.ExerciseUser;
import com.score.backend.services.ExerciseService;
import com.score.backend.services.LevelService;
import com.score.backend.services.UserService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.*;

@RestController
@RequiredArgsConstructor
public class ExerciseController {
    private final ExerciseService exerciseService;
    private final LevelService levelService;
    private final UserService userService;

    @RequestMapping(value = "/score/exercise/walking/save", method = POST)
    public ResponseEntity<Object> uploadWalkingFeed(WalkingDto walkingDto, HttpServletResponse response) {
        // 피드 저장
        exerciseService.saveFeed(walkingDto);
        // 유저의 연속 운동 일수 증가
        exerciseService.increaseConsecutiveDate(walkingDto.getAgentId());
        // 누적 운동 거리에 따른 포인트 증가
        levelService.increasePointsByWalkingDistance(walkingDto.getAgentId(), walkingDto.getDistance());
        // 누적 운동 거리 업데이트
        exerciseService.cumulateExerciseDistance(walkingDto.getAgentId(), walkingDto.getDistance());
        // 누적 운동 시간 업데이트
        exerciseService.cumulateExerciseDuration(walkingDto.getAgentId(), walkingDto.getStartedAt(), walkingDto.getCompletedAt());
        // 피드 업로드에 따른 포인트 증가
        levelService.increasePointsForTodaysFirstExercise(walkingDto.getAgentId());
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation(URI.create("http://localhost:8080/score/main"));
        return new ResponseEntity<>(response, httpHeaders, HttpStatus.MOVED_PERMANENTLY);
    }
    @RequestMapping(value = "/score/exercise/walking/delete", method = DELETE)
    public ResponseEntity<Object> deleteFeed(@RequestParam("id") Long id) {
        Exercise feed = exerciseService.findFeedByExerciseId(id).orElseThrow(
                () -> new RuntimeException("Exercise not found")
        );
        exerciseService.deleteFeed(feed);
        // 피드 삭제 후 어느 페이지로 이동할 것인지 설정 필요
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
