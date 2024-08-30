package com.score.backend.services;

import com.score.backend.models.User;
import com.score.backend.repositories.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
@Transactional
public class FriendService {
    private final UserRepository userRepository;

    public void saveNewFriend(Long userId1, Long userId2) {
        User user1 = userRepository.findById(userId1).orElseThrow(
                () -> new NoSuchElementException("User not found"));
        User user2 = userRepository.findById(userId2).orElseThrow(
                () -> new NoSuchElementException("User not found"));
        user1.addFriend(user2);
    }

    public void deleteFriend(Long userId1, Long userId2) {
        User user1 = userRepository.findById(userId1).orElseThrow(
                () -> new NoSuchElementException("User not found"));
        User user2 = userRepository.findById(userId2).orElseThrow(
                () -> new NoSuchElementException("User not found"));
        user1.deleteFriend(user2);
    }

    public void blockFriend(Long userId1, Long userId2) {
        User user1 = userRepository.findById(userId1).orElseThrow(
                () -> new NoSuchElementException("User not found"));
        User user2 = userRepository.findById(userId2).orElseThrow(
                () -> new NoSuchElementException("User not found"));
        user1.blockUser(user2);
    }

    // 내가 차단한 친구 목록 조회
    @Transactional(readOnly = true)
    public List<User> findBlockingUsers(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new NoSuchElementException("User not found")
        );
        return user.getBlockedUsers();
    }

    // 내가 차단한 친구 차단 해제
    public void unblockUser(Long userId1, Long userId2) {
        User user1 = userRepository.findById(userId1).orElseThrow(
                () -> new NoSuchElementException("User not found")
        );
        User user2 = userRepository.findById(userId2).orElseThrow(
                () -> new NoSuchElementException("User not found")
        );
        user1.getBlockedUsers().remove(user2);
    }

    @Transactional(readOnly = true)
    public Page<User> getAllFriends(int page, Long userId) {
        Pageable pageable = PageRequest.of(page, 20, Sort.by(Sort.Order.desc("createdAt")));
        return userRepository.findFriendsPage(userId, pageable);
    }

    @Transactional(readOnly = true)
    public List<User> getFriendsByNicknameContaining(Long userId, String nickname) {
        return userRepository.findFriendsByNicknameContaining(userId, nickname);
    }
}
