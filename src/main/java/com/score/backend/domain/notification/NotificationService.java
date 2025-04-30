package com.score.backend.domain.notification;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import com.score.backend.domain.exercise.Exercise;
import com.score.backend.domain.exercise.TaggedUser;
import com.score.backend.domain.exercise.repositories.TaggedUserRepository;
import com.score.backend.domain.user.User;
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

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Transactional
public class NotificationService {
    private final NotificationRepository notificationRepository;
    private final TaggedUserRepository taggedUserRepository;

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

    public void getToken(User user, String token) {
        user.setFcmToken(token);
    }

    public void sendAndSaveNotification(User user, FcmMessageRequest request) {
        sendMessage(user, request);
        saveNotification(user, request);
    }

    @Transactional(readOnly = true)
    private void sendMessage(User user, FcmMessageRequest request) {
        try {
            FirebaseMessaging.getInstance().send(Message.builder()
                    .setNotification(Notification.builder()
                            .setTitle(request.getTitle())
                            .setBody(request.getBody())
                            .build())
                    .setToken(user.getFcmToken())  // 대상 디바이스의 등록 토큰
                    .build());
        } catch (Exception ignored) {
            // FCM 토큰이 null인 경우 알림을 전송하지 않고 넘어감.
            // 알림 전송에 문제가 생기더라도 이외의 로직은 상관 없이 모두 그대로 수행되어야 함.
        }
    }

    public Set<TaggedUser> notifyToTaggedUsers(Set<TaggedUser> taggedUsers, User agent) throws FirebaseMessagingException {
        for (TaggedUser taggedUser : taggedUsers) {
            // 태그된 유저들에게 알림 전송 및 알림 저장
            if (taggedUser.getUser().isTag()) {
                FcmMessageRequest fcmMessageRequest = new FcmMessageRequest(taggedUser.getUser().getId(), agent.getNickname() + "님에게 함께 운동한 사람으로 태그되었어요!", "피드를 확인해보러 갈까요?");
                sendAndSaveNotification(taggedUser.getUser(), fcmMessageRequest);
            }
        }
        return taggedUsers;
    }

    private void saveNotification(User user, FcmMessageRequest request) {
        notificationRepository.save(new com.score.backend.domain.notification.Notification(user, request.getTitle(), request.getBody()));
    }

    public void deleteNotification(Long notificationId) {
        notificationRepository.deleteById(notificationId);
    }

    public void changeNotificationReceivingStatus(User user, NotificationStatusRequest request) {
        user.setNotificationReceivingStatus(request);
    }
}
