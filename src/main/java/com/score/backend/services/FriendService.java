package com.score.backend.services;

import com.score.backend.models.User;
import com.score.backend.repositories.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FriendService {
    private final UserRepository userRepository;

    @Transactional
    public void saveNewFriend(Long userId1, Long userId2) {
        User user1 = userRepository.findById(userId1).orElseThrow(
                () -> new RuntimeException("User not found"));
        User user2 = userRepository.findById(userId2).orElseThrow(
                () -> new RuntimeException("User not found"));
        user1.addFriend(user2);
    }
}
