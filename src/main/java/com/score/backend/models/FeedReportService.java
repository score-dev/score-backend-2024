package com.score.backend.models;

import com.score.backend.models.dtos.FeedReportDto;
import com.score.backend.models.enums.FeedReportReason;
import com.score.backend.models.exercise.Exercise;
import com.score.backend.repositories.FeedReportRepository;
import com.score.backend.services.ExerciseService;
import com.score.backend.services.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class FeedReportService {
    private final UserService userService;
    private final ExerciseService exerciseService;
    private final FeedReportRepository feedReportRepository;

    @Transactional
    public void createReport(FeedReportDto feedReportDto) {
        User agent = userService.findUserById(feedReportDto.getAgentId()).orElseThrow(
                () -> new NoSuchElementException("Agent not found")
        );
        Exercise feed = exerciseService.findFeedByExerciseId(feedReportDto.getFeedId()).orElseThrow(
                () -> new NoSuchElementException("Exercise not found")
        );

        FeedReport feedReport = FeedReport.builder()
                .reportAgent(agent)
                .reportedFeed(feed)
                .reason(FeedReportReason.valueOf(feedReportDto.getReportReason()))
                .comment(feedReportDto.getComment())
                .build();
    }
}
