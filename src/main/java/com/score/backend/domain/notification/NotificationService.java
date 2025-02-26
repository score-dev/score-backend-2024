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
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
@Transactional
public class NotificationService {
    private final UserService userService;
    private final RedisTemplate<String, String> redisTemplate;
    private final NotificationRepository notificationRepository;

    private Duration calculateTTL() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime midnight = LocalDateTime.of(now.toLocalDate().plusDays(1), LocalTime.MIDNIGHT);
        return Duration.between(now, midnight);
    }
    private String generateRedisKey(Long senderId, Long receiverId) {
        return String.format("user:%d:notified:%d", senderId, receiverId);
    }

    // 알림 전송 가능 여부 확인(오늘 sender가 receiver에게 바통 찌르기를 한 기록이 없으면 true 리턴)
    @Transactional(readOnly = true)
    public boolean canSendNotification(Long senderId, Long receiverId) {
        String key = generateRedisKey(senderId, receiverId);
        return !redisTemplate.hasKey(key);
    }

    // 바통 찌르기 알림 전송 발생 시 Redis에 전송 기록 저장
    public void saveBatonLog(Long senderId, Long receiverId) {
        redisTemplate.opsForValue().set(generateRedisKey(senderId, receiverId), "true", calculateTTL());
    }

    @Transactional(readOnly = true)
    public com.score.backend.domain.notification.Notification findById(Long id) {
        return notificationRepository.findById(id).orElseThrow(NoSuchElementException::new);
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
    public String sendMessage(FcmMessageRequest request)  throws FirebaseMessagingException {
        return FirebaseMessaging.getInstance().send(Message.builder()
                .setNotification(Notification.builder()
                        .setTitle(request.getTitle())
                        .setBody(request.getBody())
                        .build())
                .setToken(userService.findUserById(request.getUserId())
                        .getFcmToken())  // 대상 디바이스의 등록 토큰
                .build());
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
