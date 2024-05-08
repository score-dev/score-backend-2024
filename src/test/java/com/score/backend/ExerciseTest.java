package com.score.backend;

import com.score.backend.models.User;
import com.score.backend.models.dtos.WalkingDto;
import com.score.backend.models.exercise.Exercise;
import com.score.backend.repositories.exercise.ExerciseRepository;
import com.score.backend.services.ExerciseService;
import com.score.backend.services.UserService;
import jakarta.transaction.Transactional;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@Transactional
@ContextConfiguration(classes = BackendApplication.class)
public class ExerciseTest {

    @Autowired
    private ExerciseService exerciseService;
    @Autowired
    private ExerciseRepository exerciseRepository;
    @Autowired
    private UserService userService;

    @Test
    public void findAllExerciseTest() {
        User testid1 = userService.findUserByNickname("testid1").get();

        WalkingDto walkingDto1 = new WalkingDto(
                LocalDateTime.of(2024, 5, 5, 13, 25),
                LocalDateTime.of(2024, 5, 5, 15, 0),
                testid1.getId(),
                null,
                100,
                "somewhere",
                "sunny",
                20,
                "happy",
                null,
                "testexercise1"
        );
        Long walkingId1 = exerciseService.saveFeed(walkingDto1);
        Optional<Exercise> feedByExerciseId = exerciseService.findFeedByExerciseId(walkingId1);
        assertThat(feedByExerciseId.isPresent()).isTrue();
        assertThat(feedByExerciseId.get().getAgent()).isSameAs(testid1);
    }

    @Test
    public void findUsersTodaysExerciseTest() {
        User testid1 = userService.findUserByNickname("testid1").get();
        User testid2 = userService.findUserByNickname("testid2").get();

        WalkingDto walkingDto1 = new WalkingDto(
                LocalDateTime.of(2024, 5, 5, 13, 25),
                LocalDateTime.of(2024, 5, 5, 15, 0),
                testid1.getId(),
                null,
                100,
                "somewhere",
                "sunny",
                20,
                "happy",
                null,
                "testexercise1"
        );
        WalkingDto walkingDto2 = new WalkingDto(
                LocalDateTime.of(2024, 5, 8, 9, 25),
                LocalDateTime.of(2024, 5, 8, 11, 0),
                testid1.getId(),
                null,
                100,
                "somewhere",
                "sunny",
                20,
                "happy",
                null,
                "testexercise2"
        );
        WalkingDto walkingDto3 = new WalkingDto(
                LocalDateTime.of(2024, 5, 8, 13, 25),
                LocalDateTime.of(2024, 5, 8, 15, 0),
                testid1.getId(),
                null,
                100,
                "somewhere",
                "sunny",
                20,
                "happy",
                null,
                "testexercise3"
        );

        WalkingDto walkingDto4 = new WalkingDto(
                LocalDateTime.of(2024, 5, 8, 13, 25),
                LocalDateTime.of(2024, 5, 8, 14, 0),
                testid2.getId(),
                null,
                100,
                "somewhere",
                "sunny",
                20,
                "happy",
                null,
                "testexercise4"
        );
        Long id1 = exerciseService.saveFeed(walkingDto1);
        Long id2 = exerciseService.saveFeed(walkingDto2);
        Long id3 = exerciseService.saveFeed(walkingDto3);
        Long id4 = exerciseService.saveFeed(walkingDto4);
        List<Exercise> result = exerciseRepository.findUsersExerciseToday(testid1.getId(), LocalDateTime.of(2024, 5, 8, 15, 0));
        assertThat(result.size()).isEqualTo(2);
        assertThat(result.get(0).getId()).isEqualTo(id2);
        assertThat(result.get(1).getId()).isEqualTo(id3);
    }
}
