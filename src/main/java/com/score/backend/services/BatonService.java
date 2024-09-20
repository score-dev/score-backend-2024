package com.score.backend.services;

import com.google.firebase.messaging.FirebaseMessagingException;
import com.score.backend.models.User;
import com.score.backend.models.dtos.FcmMessageRequest;
import com.score.backend.repositories.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class BatonService {
    private final UserService userService;
    private final UserRepository userRepository;
    private final NotificationService notificationService;

    // 그룹 내에서 오늘 운동을 쉰(== 3분 이상 운동하지 않은) 유저 목록 조회
    @Transactional(readOnly = true)
    public List<User> findAllMembersWhoDidNotExerciseToday(Long groupId) {
        return userRepository.findGroupMatesWhoDidNotExerciseToday(groupId);
    }

    // 바통 찌르기
    public boolean turnOverBaton(Long senderId, Long receiverId) throws FirebaseMessagingException {
        User sender = userService.findUserById(senderId).orElseThrow(
                () -> new NoSuchElementException("User Not Found.")
        );
        if (!notificationService.canSendNotification(senderId, receiverId)) {
            return false;
        }
        FcmMessageRequest message = new FcmMessageRequest(receiverId, sender.getNickname() + "님이 바통을 찔렀어요!", "오늘치 운동하러 스코어와 떠나 볼까요?");
        notificationService.sendMessage(message);
        notificationService.saveNotification(message);
        notificationService.saveBatonLog(senderId, receiverId);

        return true;
    }
}
