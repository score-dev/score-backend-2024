package com.score.backend.domain.group;

import com.google.firebase.messaging.FirebaseMessagingException;
import com.score.backend.domain.notification.NotificationService;
import com.score.backend.domain.user.User;
import com.score.backend.domain.user.UserService;
import com.score.backend.dtos.BatonStatusDto;
import com.score.backend.dtos.FcmMessageRequest;
import com.score.backend.domain.user.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@Transactional
@RequiredArgsConstructor
public class BatonService {
    private final UserService userService;
    private final UserRepository userRepository;
    private final NotificationService notificationService;

    /* sender: 바통을 찌르려는 사람, receiver: sender에 의해 바통에 찔리게 될 사람 */

    // 그룹 내에서 오늘 운동을 쉰(== 3분 이상 운동하지 않은) 유저 목록 조회
    @Transactional(readOnly = true)
    public List<User> findAllMembersWhoDidNotExerciseToday(Long groupId) {
        return userRepository.findGroupMatesWhoDidNotExerciseToday(groupId);
    }

    // 그룹 내 오늘 운동 쉰 유저 목록과 각 유저들이 sender에게 이미 바통을 찔렀는지 여부를 dto로 반환
    @Transactional(readOnly = true)
    public List<BatonStatusDto> getBatonStatuses(Long senderId, Long groupId) {
        List<User> notExercisedUsers = findAllMembersWhoDidNotExerciseToday(groupId);
        List<BatonStatusDto> batonStatuses = new ArrayList<>();

        for (User user : notExercisedUsers) {
            // 자기 자신은 목록에 포함하지 않음.
            if (user.getId().equals(senderId)) {
                continue;
            }
            batonStatuses.add(new BatonStatusDto(user.getId(), user.getNickname(), user.getProfileImg(),
                    notificationService.canSendNotification(senderId, user.getId())));
        }
        return batonStatuses;
    }

    // 바통 찌르기
    public boolean turnOverBaton(Long senderId, Long receiverId) throws FirebaseMessagingException {
        User sender = userService.findUserById(senderId).orElseThrow(
                () -> new NoSuchElementException("요청을 보낸 유저를 찾을 수 없습니다.")
        );
        if (!notificationService.canSendNotification(senderId, receiverId)) {
            return false;
        }
        notificationService.saveBatonLog(senderId, receiverId);
        FcmMessageRequest message = new FcmMessageRequest(receiverId, sender.getNickname() + "님이 바통을 찔렀어요!", "오늘치 운동하러 스코어와 떠나 볼까요?");
        notificationService.sendMessage(message);
        notificationService.saveNotification(message);
        notificationService.saveBatonLog(senderId, receiverId);

        return true;
    }
}
