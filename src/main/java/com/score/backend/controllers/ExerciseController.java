package com.score.backend.controllers;

import com.score.backend.models.User;
import com.score.backend.models.dtos.WalkingDto;
import com.score.backend.models.exercise.Exercise;
import com.score.backend.models.exercise.ExerciseUser;
import com.score.backend.services.ExerciseService;
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
    private final UserService userService;

    @RequestMapping(value = "/score/exercise/walking/save", method = POST)
    public ResponseEntity<Object> uploadWalkingFeed(WalkingDto walkingDto, HttpServletResponse response) {
        // 새로운 피드 엔티티 생성
        Exercise feed = walkingDto.toEntity();
        // 운동한 유저(피드 작성자) db에서 찾기
        User agent = userService.findUserById(walkingDto.getAgentId()).orElseThrow(
                () -> new RuntimeException("Agent not found")
        );

        // agent와 함께 운동한 유저의 id 값을 가지고 db에서 찾기
        List<ExerciseUser> exerciseUsers = new ArrayList<>();
        for (Long id : walkingDto.getOthersId()) {
            User user = userService.findUserById(id).orElseThrow(
                    () -> new RuntimeException("User not found")
            );
            exerciseUsers.add(new ExerciseUser(user));
        }
        // 피드 작성자, 함께 운동한 친구 설정
        feed.setAgentAndExerciseUser(agent, exerciseUsers);
        exerciseService.saveFeed(feed);
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
