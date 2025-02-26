package com.score.backend.domain.report.userreport;

import com.score.backend.dtos.UserReportDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<String> reportUser(@Parameter(required = true, description = "신고 요청에 필요한 정보가 저장된 dto") @RequestBody UserReportDto userReportDto) {
        userReportService.createReport(userReportDto);
        return ResponseEntity.ok("유저 신고가 완료되었습니다.");
    }
}
