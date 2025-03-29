package com.score.backend.domain.notification;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import com.score.backend.domain.user.User;
import com.score.backend.domain.user.UserService;
import com.score.backend.dtos.FcmMessageRequest;
import com.score.backend.dtos.FcmNotificationResponse;
import com.score.backend.dtos.NotificationStatusRequest;
import com.score.backend.exceptions.ExceptionType;
import com.score.backend.exceptions.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class NotificationService {
    private final UserService userService;
    private final NotificationRepository notificationRepository;

    @Transactional(readOnly = true)
    public com.score.backend.domain.notification.Notification findById(Long id) {
        return notificationRepository.findById(id).orElseThrow(
                () -> new NotFoundException(ExceptionType.NOTIFICATION_NOT_FOUND));
    }

    @Transactional(readOnly = true)
    public Page<FcmNotificationResponse> findAllByUserId(Long userId, int page) {
        Pageable pageable = PageRequest.of(page, 25, Sort.by(Sort.Order.desc("createdAt")));
        return new FcmNotificationResponse().toDto(notificationRepository.findByAgentId(userId, pageable));
    }

    public void getToken(Long userId, String token) {
        User user = userService.findUserById(userId);
        user.setFcmToken(token);
    }

    @Transactional(readOnly = true)
    public String sendMessage(FcmMessageRequest request) throws FirebaseMessagingException {
        try {
            return FirebaseMessaging.getInstance().send(Message.builder()
                    .setNotification(Notification.builder()
                            .setTitle(request.getTitle())
                            .setBody(request.getBody())
                            .build())
                    .setToken(userService.findUserById(request.getUserId())
                            .getFcmToken())  // 대상 디바이스의 등록 토큰
                    .build());
        } catch (IllegalArgumentException e) {
            return null;
//            throw new NotFoundException(ExceptionType.FCM_TOKEN_NOT_FOUND);
        }
    }

    public void saveNotification(FcmMessageRequest request) {
        notificationRepository.save(new com.score.backend.domain.notification.Notification(userService.findUserById(request.getUserId()),
                request.getTitle(), request.getBody()));
    }

    public void deleteNotification(Long notificationId) {
        notificationRepository.deleteById(notificationId);
    }

    public void changeNotificationReceivingStatus(NotificationStatusRequest request) {
        User user = userService.findUserById(request.getUserId());
        user.setNotificationReceivingStatus(request);
    }
}
