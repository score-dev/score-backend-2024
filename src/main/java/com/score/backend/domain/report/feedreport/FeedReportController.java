package com.score.backend.domain.report.feedreport;

import com.score.backend.dtos.FeedReportDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.NoSuchElementException;

@Tag(name = "Feed Report", description = "피드 신고를 위한 API입니다.")
@RestController
@RequiredArgsConstructor
public class FeedReportController {
    private final FeedReportService feedReportService;

    @Operation(summary = "피드 신고", description = "피드를를 신고합니다.")
    @RequestMapping(value = "/score/exercise/report", method = RequestMethod.POST)
    @ApiResponses(
            value = {@ApiResponse(responseCode = "200", description = "피드 신고 완료"),
                    @ApiResponse(responseCode = "404", description = "User or Feed Not Found")})
    public ResponseEntity<String> reportFeed(@Parameter(required = true, description = "신고 요청에 필요한 정보가 저장된 dto") @RequestBody FeedReportDto feedReportDto) {
        feedReportService.createReport(feedReportDto);
        return ResponseEntity.ok("피드 신고가 완료되었습니다.");
    }
}
