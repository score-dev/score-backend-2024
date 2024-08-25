package com.score.backend.controllers;

import com.google.firebase.messaging.FirebaseMessagingException;
import com.score.backend.models.dtos.FcmMessageRequest;
import com.score.backend.models.dtos.PostTokenReq;
import com.score.backend.services.NotificationService;
import com.score.backend.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.NoSuchElementException;

@Tag(name = "Notification", description = "알림 기능 관리를 위한 API입니다.")
@RestController
@RequiredArgsConstructor
public class NotificationController {
    private final NotificationService notificationService;
    private final UserService userService;

    @Operation(summary = "FCM 토큰 발급", description = "알림 전송을 위해 유저의 FCM 토큰을 발급받아 서버에 저장합니다.")
    @ApiResponses(
            value = {@ApiResponse(responseCode = "200", description = "토큰 발급 및 저장 완료."),
                    @ApiResponse(responseCode = "404", description = "User Not Found")}
    )
    @PostMapping("/{userId}/token")
    public ResponseEntity<String> getToken(@PathVariable Long userId, @Valid @RequestBody PostTokenReq postTokenReq) {
        try {
            notificationService.getToken(userId, postTokenReq.getToken());
            return ResponseEntity.ok("토큰이 저장되었습니다.");
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "알림 전송", description = "유저에게 알림을 전송합니다.")
    @ApiResponses(
            value = {@ApiResponse(responseCode = "200", description = "알림 전송 완료"),
                    @ApiResponse(responseCode = "404", description = "User Not Found"),
                    @ApiResponse(responseCode = "404", description = "Firebase Messaging Error")
            }
    )
    @PostMapping("/score/fcm")
    public ResponseEntity<String> sendFcmNotification(FcmMessageRequest fcmMessageRequest) {
        try {
            return ResponseEntity.ok(notificationService.sendMessage(fcmMessageRequest));
        } catch (FirebaseMessagingException e1) {
            return ResponseEntity.badRequest().build();
        } catch (NoSuchElementException e2) {
            return ResponseEntity.notFound().build();
        }
    }
}
