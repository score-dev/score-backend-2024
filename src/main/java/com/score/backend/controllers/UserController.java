package com.score.backend.controllers;

import com.score.backend.models.School;
import com.score.backend.models.User;
import com.score.backend.models.dtos.SchoolDto;
import com.score.backend.models.dtos.UserDto;
import com.score.backend.models.dtos.UserUpdateDto;
import com.score.backend.security.jwt.JwtProvider;
import com.score.backend.services.SchoolService;
import com.score.backend.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.NoSuchElementException;

@Tag(name = "User", description = "회원 정보 관리를 위한 API입니다.")
@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final SchoolService schoolService;
    private final JwtProvider jwtProvider;

    // 닉네임 중복 검사
    @Operation(summary = "닉네임 중복 검사", description = "온보딩이나 회원 정보 수정 시 닉네임 중복 검사를 위한 api입니다.")
    @RequestMapping(value = "/score/{nickname}/exists", method = RequestMethod.GET)
    @ApiResponse(responseCode = "200", description = "닉네임 중복 검사 완료. ResponseBody의 내용이 0이면 필드에 아무 것도 입력되지 않은 경우, 1이면 중복되지 않은 닉네임인 경우, -1이면 이미 존재하는 닉네임인 경우.")
    public ResponseEntity<Integer> checkNicknameUniqueness(@Parameter(description = "유저가 필드에 입력한 닉네임") @PathVariable(name = "nickname") String nickname) {
        if (nickname.isEmpty()) {
            return ResponseEntity.ok(0); // 필드에 아무것도 입력되지 않은 상태인 경우
        }
        if (userService.findUserByNickname(nickname).isEmpty()) {
            return ResponseEntity.ok(1); // 중복되지 않는 닉네임인 경우
        } else {
            return ResponseEntity.ok(-1); // 이미 존재하는 닉네임인 경우
        }
    }

    // 소셜 로그인 인증 완료시
    @Operation(summary = "소셜 로그인 인증 완료", description = "소셜 로그인 인증 완료 후 기존 회원인지 여부를 응답받기 위한 api입니다.")
    @ApiResponses(
            value = {@ApiResponse(responseCode = "200", description = "소셜 로그인 인증 완료. RequestBody가 true이면 기존 회원, false이면 신규 회원."),
                    @ApiResponse(responseCode = "400", description = "Bad Request"),
                    @ApiResponse(responseCode = "404", description = "User Not Found")
            }
    )
    @RequestMapping(value = "/score/auth", method = RequestMethod.GET)
    public ResponseEntity<Boolean> authorizeUser(@RequestParam("id") @Parameter(required = true, description = "provider id") String key, HttpServletResponse response) {
        // 신규 회원이라면 온보딩 페이지로 이동
        if (!userService.isPresentUser(key)) {
            return ResponseEntity.ok(false);
        } else {
            // 기존 회원이라면 로그인 진행(토큰 갱신) 후 메인 페이지로 이동
            User user = userService.findUserByKey(key).orElseThrow(
                    () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
            login(user.getNickname(), response);
        }
        return ResponseEntity.ok(true);
    }

    // 온보딩에서 회원 정보 입력 완료시
    @Operation(summary = "신규 회원 정보 저장", description = "온보딩에서 회원 정보가 입력이 완료될 경우 수행되는 요청입니다. 해당 정보를 db에 저장하고 로그인을 진행합니다.")
    @ApiResponses(
            value = {@ApiResponse(responseCode = "200", description = "신규 회원 정보 저장 완료, 소셜 로그인 인증 페이지로 리다이렉트", headers = {@Header(name = "new URI", schema = @Schema(type = "string"))}),
                    @ApiResponse(responseCode = "400", description = "Bad Request")}
    )
    @RequestMapping(value = "/score/onboarding/fin", method = RequestMethod.POST)
    public ResponseEntity<Object> saveNewUser(@Parameter(description = "회원 정보 전달을 위한 DTO", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)) @RequestPart(value = "userDto") UserDto userDto,
                                              @Parameter(description = "학교 정보 전달을 위한 DTO", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)) @RequestPart(value = "schoolDto") SchoolDto schoolDto,
                                              @Parameter(description = "프로필 사진", content = @Content(mediaType = MediaType.MULTIPART_FORM_DATA_VALUE)) @RequestPart(value = "file") MultipartFile multipartFile, HttpServletResponse response) throws IOException {

        // 유저의 학교 정보가 이미 db에 존재하면 그 학교 정보를 찾기, 없으면 새로운 학교 엔티티 생성하기.
        School school = schoolService.findSchoolByCode(schoolDto.getSchoolCode());
        if (school == null) {
            school = schoolService.saveSchool(schoolDto);
        }
        // 유저 엔티티에 학교 정보 set
        User user = userDto.toEntity();
        user.setSchoolAndStudent(school);

        // 해당 회원 정보 db에 저장
        userService.saveUser(user, multipartFile);

        // 소셜 로그인 인증 완료시 호출되는 페이지로 이동해 로그인 진행
        response.getOutputStream().close();
        response.addHeader(HttpHeaders.LOCATION, "http://23.130.135.106:8081//score/onboarding/fin/school");
        return new ResponseEntity<>(response, HttpStatus.MOVED_PERMANENTLY);
    }

    private void login(String nickname, HttpServletResponse response) {
        userService.findUserByNickname(nickname).ifPresentOrElse(
                // 로그인 시도한 회원이 기존 회원이라면 access token과 refresh token 갱신
                user -> {
                    List<String> tokens = jwtProvider.getNewToken(nickname);
                    // user entity update 로직 구현 후 refresh token update 로직 추가 필요
                    response.setHeader(HttpHeaders.AUTHORIZATION, tokens.get(0));
                },
                () -> {}); // 예외 처리 필요
    }

    // 메인 페이지 접속시 토큰의 유효성 확인
    // 메인 페이지 기능 구현되면 추후 수정 필요
    @Operation(summary = "메인 페이지", description = "메인 페이지 접속 요청 발생시 jwt 토큰을 검증합니다.")
    @ApiResponses(
            value = {@ApiResponse(responseCode = "200", description = "유저 인증 완료"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized")}
    )
    @RequestMapping(value = "/score/main", method = RequestMethod.GET)
    public ResponseEntity<Object> verifyUser(@RequestHeader(name = "Authorization") @Parameter(required = true, description = "Request Header의 Authorization에 있는 jwt 토큰") String token,
                                             HttpServletResponse response) {
        if (jwtProvider.validateToken(token, response)) {
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

    }

    // 회원 탈퇴
    @Operation(summary = "회원 탈퇴", description = "회원 탈퇴 요청 발생시 해당 회원의 모든 정보를 db에서 삭제합니다.")
    @ApiResponses(
            value = {@ApiResponse(responseCode = "200", description = "회원 탈퇴 완료"),
                    @ApiResponse(responseCode = "404", description = "User Not Found")})
    @RequestMapping(value = "/score/user/withdrawal", method = RequestMethod.DELETE)
    public ResponseEntity<HttpStatus> withdrawUser(@Parameter(description = "회원 탈퇴를 요청한 유저의 고유 id 값") @RequestParam(name = "id") Long id,
                                                   @Parameter(description = "회원 탈퇴 사유") @RequestParam(name = "reason") String reason) {
        try {
            userService.withdrawUser(id);
            Logger logger = LoggerFactory.getLogger("withdrawal-logger");
            logger.info("User ID: {}, Reason: {}", id, reason);
        } catch(NoSuchElementException e) {
            return new ResponseEntity<>(HttpStatusCode.valueOf(404));
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // 회원 정보 수정
    @Operation(summary = "회원 정보 수정", description = "수정된 회원 정보를 db에 업데이트합니다.")
    @ApiResponses(
            value = {@ApiResponse(responseCode = "200", description = "회원 정보 수정 완료"),
                    @ApiResponse(responseCode = "409", description = "마지막 학교 정보 수정 후 30일이 경과되기 전 학교 정보 수정 시도"),
                    @ApiResponse(responseCode = "404", description = "User Not Found")
            })
    @RequestMapping(value = "/score/user/update/{id}", method = RequestMethod.POST)
    public ResponseEntity<Object> updateUserInfo(@Parameter(description = "회원 정보 수정을 요청한 유저의 고유 id 값") @PathVariable(name = "id") Long userId,
                                                 @Parameter(description = "수정된 회원 정보 전달을 위한 DTO", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)) @RequestPart(value = "userUpdateDto") UserUpdateDto userUpdateDto,
                                                 @Parameter(description = "프로필 사진", content = @Content(mediaType = MediaType.MULTIPART_FORM_DATA_VALUE)) @RequestPart(value = "file") MultipartFile multipartFile) {
        if (userService.findUserById(userId).isEmpty()) {
            return new ResponseEntity<>(HttpStatusCode.valueOf(404));
        }
        User user = userService.findUserById(userId).get();

        if (!user.getSchool().getSchoolCode().equals(userUpdateDto.getSchool().getSchoolCode())
                && ChronoUnit.DAYS.between(LocalDateTime.now(), user.getSchool().getUpdatedAt()) < 30) {
            return new ResponseEntity<>(HttpStatusCode.valueOf(409));
        }
        userService.updateUser(userId, userUpdateDto, multipartFile);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
