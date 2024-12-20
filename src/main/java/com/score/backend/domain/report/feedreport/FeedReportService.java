package com.score.backend.domain.report.feedreport;

import com.score.backend.domain.exercise.ExerciseService;
import com.score.backend.domain.user.UserService;
import com.score.backend.domain.user.User;
import com.score.backend.dtos.FeedReportDto;
import com.score.backend.domain.exercise.Exercise;
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
        Exercise feed = exerciseService.findFeedByExerciseId(feedReportDto.getFeedId());

        FeedReport feedReport = FeedReport.builder()
                .reportAgent(agent)
                .reportedFeed(feed)
                .reason(FeedReportReason.valueOf(feedReportDto.getReportReason()))
                .comment(feedReportDto.getComment())
                .build();
    }
}
