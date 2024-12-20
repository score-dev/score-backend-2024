package com.score.backend.domain.report.userreport;

import com.score.backend.domain.report.userreport.repositories.UserReportRepository;
import com.score.backend.domain.user.User;
import com.score.backend.domain.user.UserService;
import com.score.backend.dtos.UserReportDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
@Transactional
public class UserReportService {
    private final UserService userService;
    private final UserReportRepository userReportRepository;

    public void createReport(UserReportDto userReportDto) {
        User agent = userService.findUserById(userReportDto.getAgentId()).orElseThrow(
                () -> new NoSuchElementException("User Not Found"));
        User object = userService.findUserById(userReportDto.getAgentId()).orElseThrow(
                () -> new NoSuchElementException("User Not Found"));

        UserReport userReport = UserReport.builder()
                .reportAgent(agent)
                .reportObject(object)
                .reason(UserReportReason.valueOf(userReportDto.getReportReason()))
                .comment(userReportDto.getComment())
                .build();
        userReportRepository.save(userReport);
    }

}
