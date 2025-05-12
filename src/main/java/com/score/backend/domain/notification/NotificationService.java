package com.score.backend.domain.notification;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import com.score.backend.domain.user.User;
import com.score.backend.domain.user.repositories.UserRepository;
import com.score.backend.dtos.FcmNotificationResponse;
import com.score.backend.dtos.NotificationDto;
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
    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public User findSystemUser() {
        return userRepository.findById(1L).orElseThrow(
                () -> new NotFoundException(ExceptionType.SYSTEM_USER_NOT_FOUND)
        );
    }

    @Transactional(readOnly = true)
    public com.score.backend.domain.notification.Notification findNotificationById(Long id) {
        return notificationRepository.findById(id).orElseThrow(
                () -> new NotFoundException(ExceptionType.NOTIFICATION_NOT_FOUND));
    }

    @Transactional(readOnly = true)
    public Page<FcmNotificationResponse> findAllByUserId(Long userId, int page) {
        Pageable pageable = PageRequest.of(page, 25, Sort.by(Sort.Order.desc("createdAt")));
        return new FcmNotificationResponse().toDto(notificationRepository.findByReceiverId(userId, pageable));
    }

    @Transactional(readOnly = true)
    public void getToken(User user, String token) {
        user.setFcmToken(token);
    }

    public void sendAndSaveNotification(NotificationDto dto) {
        sendMessage(saveNotification(dto));
    }

    private void sendMessage(com.score.backend.domain.notification.Notification notification) {
        try {
            FirebaseMessaging.getInstance().send(Message.builder()
                    .setNotification(Notification.builder()
                            .setTitle(notification.getTitle())
                            .setBody(notification.getBody())
                            .build())
                    .setToken(notification.getReceiver().getFcmToken())  // 대상 디바이스의 등록 토큰
                    .build());
        } catch (Exception ignored) {
            // FCM 토큰이 null인 경우 알림을 전송하지 않고 넘어감.
            // 알림 전송에 문제가 생기더라도 이외의 로직은 상관 없이 모두 그대로 수행되어야 함.
        }
    }

    private com.score.backend.domain.notification.Notification saveNotification(NotificationDto dto) {
        return notificationRepository.save(com.score.backend.domain.notification.Notification.builder()
                        .sender((dto.getSender() == null)? findSystemUser() : dto.getSender())
                        .receiver(dto.getReceiver())
                        .type(dto.getType())
                        .relatedGroup(dto.getRelatedGroup())
                        .relatedFeed((dto.getRelatedFeed() == null)? null : dto.getRelatedFeed())
                        .title(dto.getTitle())
                        .body(dto.getBody())
                        .build());
    }

    public void deleteNotification(Long notificationId) {
        notificationRepository.deleteById(notificationId);
    }

    public void changeNotificationReceivingStatus(User user, NotificationStatusRequest request) {
        user.setNotificationReceivingStatus(request);
    }

    public void changeNotificationReadingStatus(Long notificationId) {
        com.score.backend.domain.notification.Notification notification = findNotificationById(notificationId);
        notification.setRead(true);
        notificationRepository.save(notification);
    }

    public void changeGroupJoinAcceptanceStatus(Long notificationId, boolean wasAccepted) {
        com.score.backend.domain.notification.Notification notification = findNotificationById(notificationId);
        notification.setJoinRequestAccepted(wasAccepted);
        notificationRepository.save(notification);
    }
}
