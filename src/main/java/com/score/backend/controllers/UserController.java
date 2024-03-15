package com.score.backend.controllers;


import com.score.backend.models.User;
import com.score.backend.security.jwt.JwtProvider;
import com.score.backend.services.UserService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final JwtProvider jwtProvider;


    @RequestMapping(value = "/score/auth", method = RequestMethod.GET)
    public ResponseEntity<Object> getUserLoginKey(@RequestParam("id") String key, HttpServletResponse response) {
        HttpHeaders httpHeaders = identifyNewUser(key, response);
        return new ResponseEntity<>(response, httpHeaders, HttpStatus.MOVED_PERMANENTLY);
    }

    public HttpHeaders identifyNewUser(String key, HttpServletResponse response) {
        Optional<User> userOption = userService.findUserByKey(key);
        HttpHeaders httpHeaders = new HttpHeaders();

        userOption.ifPresentOrElse(
                // if present: 일치하는 회원 데이터가 존재하는 경우 -> 인증 수행 후 메인 페이지로 이동
                user -> {
                    boolean isAuthenticated = jwtProvider.validateToken(user.getLoginKey(), response);
                    if (isAuthenticated) {
                        httpHeaders.setLocation(URI.create("http://localhost:8080/score/main"));
                    }
                },
                // or else: 존재하지 않는 회원인 경우 -> 회원 정보 입력 페이지로 이동
                () -> httpHeaders.setLocation(URI.create("http://localhost:8080/score/onbording")));
        return httpHeaders;

    }
}
