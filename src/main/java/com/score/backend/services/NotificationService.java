package com.score.backend.services;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import com.score.backend.models.User;
import com.score.backend.models.dtos.FcmMessageRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class NotificationService {
    private final UserService userService;

    @Transactional
    public void getToken(Long userId, String token) {
        User user = userService.findUserById(userId).orElseThrow(
                () -> new NoSuchElementException("User not found")
        );
        user.setFcmToken(token);
    }

    public String sendMessage(FcmMessageRequest request)  throws FirebaseMessagingException {
        return FirebaseMessaging.getInstance().send(Message.builder()
                .setNotification(Notification.builder()
                        .setTitle(request.getTitle())
                        .setBody(request.getBody())
                        .build())
                .setToken(userService.findUserById(request.getUserId()).orElseThrow(
                        () -> new NoSuchElementException("User not found")
                ).getFcmToken())  // 대상 디바이스의 등록 토큰
                .build());
    }


}
