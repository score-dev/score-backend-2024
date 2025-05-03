package com.score.backend.domain.report.userreport;

import com.score.backend.domain.report.userreport.repositories.UserReportRepository;
import com.score.backend.domain.user.User;
import com.score.backend.dtos.UserReportDto;
import com.score.backend.exceptions.ExceptionType;
import com.score.backend.exceptions.ScoreCustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserReportService {
    private final UserReportRepository userReportRepository;

    public void createReport(User agent, User object, UserReportDto userReportDto) {
        if (agent.equals(object)) {
            throw new ScoreCustomException(ExceptionType.SELF_REPORT);
        }
        UserReport userReport = UserReport.builder()
                .reportAgent(agent)
                .reportObject(object)
                .reason(UserReportReason.valueOf(userReportDto.getReportReason()))
                .comment(userReportDto.getComment())
                .build();
        userReportRepository.save(userReport);
    }

    public int getUserReportCount(User user) {
        return userReportRepository.findDistinctReportCounts(user.getId());
    }
}
