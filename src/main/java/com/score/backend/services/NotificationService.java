package com.score.backend.services;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import com.score.backend.models.User;
import com.score.backend.models.dtos.FcmMessageRequest;
import com.score.backend.repositories.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
@Transactional
public class NotificationService {
    private final UserService userService;
    private final NotificationRepository notificationRepository;

    public com.score.backend.models.Notification findById(Long id) {
        return notificationRepository.findById(id).orElseThrow(NoSuchElementException::new);
    }



    public void getToken(Long userId, String token) {
        User user = userService.findUserById(userId).orElseThrow(
                () -> new NoSuchElementException("User not found")
        );
        user.setFcmToken(token);
    }

    @Transactional(readOnly = true)
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

    public void saveNotification(FcmMessageRequest request) {
        notificationRepository.save(new com.score.backend.models.Notification(userService.findUserById(request.getUserId()).orElseThrow(
                () -> new NoSuchElementException("User not found")
        ), request.getTitle(), request.getBody()));
    }

    public void deleteNotification(Long notificationId) {
        notificationRepository.deleteById(notificationId);
    }
}
