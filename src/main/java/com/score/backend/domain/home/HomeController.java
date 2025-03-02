package com.score.backend.domain.home;

import com.score.backend.dtos.home.HomeResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Home", description = "홈 화면을 위한 API입니다.")
@RestController
@RequiredArgsConstructor
public class HomeController {
    private final HomeService homeService;

    @Operation(summary = "홈 화면", description = "홈 화면 출력을 위한 데이터를 제공하는 API입니다.")
    @PostMapping("/score/home")
    public ResponseEntity<HomeResponse> home(@RequestParam("id") @Parameter(required = true, description = "요청한 유저의 고유 번호") Long userId) {
        return ResponseEntity.ok(homeService.getHomeInfo(userId));
    }
}
