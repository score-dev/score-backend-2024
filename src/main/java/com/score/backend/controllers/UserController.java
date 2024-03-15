package com.score.backend.controllers;


import com.score.backend.models.User;
import com.score.backend.models.dtos.UserDto;
import com.score.backend.security.jwt.JwtProvider;
import com.score.backend.services.UserService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final JwtProvider jwtProvider;

    // 소셜 로그인 인증 완료시
    @RequestMapping(value = "/score/auth", method = RequestMethod.GET)
    public ResponseEntity<Object> authorizeUser(@RequestParam("id") String key, HttpServletResponse response) {
        HttpHeaders httpHeaders = new HttpHeaders();

        // 신규 회원이라면 온보딩 페이지로 이동
        if (!userService.isPresentUser(key)) {
            httpHeaders.setLocation(URI.create("http://localhost:8080/score/onbording"));
        } else {
            // 기존 회원이라면 로그인 진행(토큰 갱신) 후 메인 페이지로 이동
            User user = userService.findUserByKey(key).orElseThrow(
                    () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
            login(user.getNickname(), response);
            httpHeaders.setLocation(URI.create("http://localhost:8080/score/main"));
        }
        return new ResponseEntity<>(response, httpHeaders, HttpStatus.MOVED_PERMANENTLY);
    }

    // 온보딩에서 회원 정보 입력 완료시
    @RequestMapping(value = "/score/onboarding/fin", method = RequestMethod.POST)
    public ResponseEntity<Object> saveNewUser(@RequestBody UserDto userDto, HttpServletResponse response) {
        // 해당 회원 정보 db에 저장
        userService.saveUser(userDto.toEntity());
        HttpHeaders httpHeaders = new HttpHeaders();
        // 소셜 로그인 인증 완료시 호출되는 페이지로 이동해 로그인 진행
        httpHeaders.setLocation(URI.create("http://localhost/score/auth?" + userDto.getLoginKey()));
        return new ResponseEntity<>(response, httpHeaders, HttpStatus.MOVED_PERMANENTLY);
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
    @RequestMapping(value = "/score/main", method = RequestMethod.GET)
    public ResponseEntity<Object> verifyUser(@RequestHeader(name = "Authorization") String token, HttpServletResponse response) {
        if (jwtProvider.validateToken(token, response)) {
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

    }
}
