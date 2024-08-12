package com.score.backend.controllers;

import com.score.backend.models.dtos.UserReportDto;
import com.score.backend.services.UserReportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;

@Tag(name = "User Report", description = "유저 신고를 위한 API입니다.")
@RestController
@RequiredArgsConstructor
public class UserReportController {
    private final UserReportService userReportService;

    @Operation(summary = "유저 신고", description = "유저를 신고합니다.")
    @RequestMapping(value = "/score/user/report", method = RequestMethod.POST)
    @ApiResponses(
            value = {@ApiResponse(responseCode = "200", description = "유저 신고 완료"),
                    @ApiResponse(responseCode = "404", description = "User Not Found")})
    public ResponseEntity<HttpStatus> reportUser(@Parameter(required = true, description = "신고 요청에 필요한 정보가 저장된 dto") @RequestBody UserReportDto userReportDto) {
        try {
            userReportService.createReport(userReportDto);
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
