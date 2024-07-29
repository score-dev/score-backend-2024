package com.score.backend.services;

import com.score.backend.models.User;
import com.score.backend.models.UserReport;
import com.score.backend.models.dtos.UserReportDto;
import com.score.backend.models.enums.UserReportReason;
import com.score.backend.repositories.user.UserRepository;
import com.score.backend.repositories.userreport.UserReportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
@Transactional
public class UserReportService {
    private final UserRepository userRepository;
    private final UserReportRepository userReportRepository;

    public void createReport(UserReportDto userReportDto) {
        User agent = userRepository.findById(userReportDto.getAgentId()).orElseThrow(
                () -> new NoSuchElementException("User Not Found"));
        User object = userRepository.findById(userReportDto.getAgentId()).orElseThrow(
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
