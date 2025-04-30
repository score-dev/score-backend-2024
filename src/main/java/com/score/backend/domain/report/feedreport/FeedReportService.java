package com.score.backend.domain.report.feedreport;

import com.score.backend.domain.user.User;
import com.score.backend.dtos.FeedReportDto;
import com.score.backend.domain.exercise.Exercise;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FeedReportService {
    private final FeedReportRepository feedReportRepository;

    @Transactional
    public void createReport(User agent, Exercise feed, FeedReportDto feedReportDto) {
        FeedReport feedReport = FeedReport.builder()
                .reportAgent(agent)
                .reportedFeed(feed)
                .reason(FeedReportReason.valueOf(feedReportDto.getReportReason()))
                .comment(feedReportDto.getComment())
                .build();
        feedReportRepository.save(feedReport);
    }
}
