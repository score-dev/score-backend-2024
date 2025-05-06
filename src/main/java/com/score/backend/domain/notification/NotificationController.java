package com.score.backend.domain.notification;

import com.score.backend.domain.user.UserService;
import com.score.backend.dtos.FcmMessageRequest;
import com.score.backend.dtos.FcmNotificationResponse;
import com.score.backend.dtos.NotificationDto;
import com.score.backend.dtos.PostTokenReq;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Notification", description = "알림 기능 관리를 위한 API입니다.")
@RestController
@RequiredArgsConstructor
public class NotificationController {
    private final UserService userService;
    private final NotificationService notificationService;

    @Operation(summary = "FCM 토큰 발급", description = "알림 전송을 위해 유저의 FCM 토큰을 발급받아 서버에 저장합니다.")
    @ApiResponses(
            value = {@ApiResponse(responseCode = "200", description = "토큰 발급 및 저장 완료."),
                    @ApiResponse(responseCode = "404", description = "User Not Found")}
    )
    @PostMapping("/{userId}/token")
    public ResponseEntity<String> getToken(@PathVariable Long userId, @Valid @RequestBody PostTokenReq postTokenReq) {
        notificationService.getToken(userService.findUserById(userId), postTokenReq.getToken());
        return ResponseEntity.ok("토큰이 저장되었습니다.");
    }

    @Operation(summary = "알림 전송", description = "유저에게 알림을 전송합니다.")
    @ApiResponses(
            value = {@ApiResponse(responseCode = "200", description = "알림 전송 완료"),
                    @ApiResponse(responseCode = "404", description = "User Not Found"),
                    @ApiResponse(responseCode = "400", description = "Firebase Messaging Error")
            }
    )
    @PostMapping("/score/fcm")
    public ResponseEntity<String> sendFcmNotification(FcmMessageRequest fcmMessageRequest) {
        NotificationDto dto = NotificationDto.builder()
                .sender(notificationService.findSystemUser())
                .receiver(userService.findUserById(fcmMessageRequest.getUserId()))
                .title(fcmMessageRequest.getTitle())
                .body(fcmMessageRequest.getBody())
                .type(NotificationType.ETC)
                .build();
        notificationService.sendAndSaveNotification(dto);
        return ResponseEntity.ok("알림 전송이 완료되었습니다.");
    }

    @Operation(summary = "알림 목록 조회", description = "유저의 알림 목록을 페이지 단위로 조회합니다.")
    @ApiResponses(
            value = {@ApiResponse(responseCode = "200", description = "알림 목록 응답 완료"),
                    @ApiResponse(responseCode = "404", description = "User Not Found"),
            }
    )
    @GetMapping("/score/fcm/list")
    public ResponseEntity<Page<FcmNotificationResponse>> getUsersNotifications(@RequestParam Long userId, @RequestParam(required = false, defaultValue = "0") int page) {
        return ResponseEntity.ok(notificationService.findAllByUserId(userId, page));
    }

    @Operation(summary = "알림 삭제", description = "특정 알림을 삭제합니다.")
    @ApiResponses(
            value = {@ApiResponse(responseCode = "200", description = "알림 삭제 완료"),
                    @ApiResponse(responseCode = "404", description = "Notification Not Found"),
            }
    )
    @PostMapping("/score/fcm/delete")
    public ResponseEntity<String> deleteFcmNotification(@RequestParam Long notificationId) {
        notificationService.deleteNotification(notificationId);
        return ResponseEntity.ok("알림 삭제가 완료되었습니다.");
    }

    @Operation(summary = "읽은 알림으로 상태 변경", description = "특정 알림을 읽은 알림으로 설정합니다.")
    @ApiResponses(
            value = {@ApiResponse(responseCode = "200", description = "알림 상태 변경 완료"),
                    @ApiResponse(responseCode = "404", description = "Notification Not Found"),
            }
    )
    @PutMapping("/score/fcm/change-status")
    public ResponseEntity<String> changeNotificationStatus(@RequestParam Long notificationId) {
        notificationService.changeNotificationReadingStatus(notificationId);
        return ResponseEntity.ok("확인된 알림으로 변경되었습니다.");
    }
}
