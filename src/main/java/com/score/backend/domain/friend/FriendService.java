package com.score.backend.domain.friend;

import com.score.backend.domain.user.User;
import com.score.backend.domain.user.repositories.UserRepository;
import com.score.backend.dtos.FriendsSearchResponse;
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
    private final FriendRepository friendRepository;
    private final UserRepository userRepository;

    public void saveNewFriend(Long userId1, Long userId2) {
        User user1 = userRepository.findById(userId1).orElseThrow(
                () -> new NoSuchElementException("User not found"));
        User user2 = userRepository.findById(userId2).orElseThrow(
                () -> new NoSuchElementException("User not found"));
        user1.addFriend(user2);
    }

    public void deleteFriend(Long userId1, Long userId2) {
        Friend user1 = friendRepository.findByUserIdAndFriendId(userId1, userId2).orElseThrow(
                () -> new NoSuchElementException("User not found")
        );
        Friend user2 = friendRepository.findByUserIdAndFriendId(userId2, userId1).orElseThrow(
                () -> new NoSuchElementException("User not found")
        );
        user1.getUser().deleteFriend(user1, user2);
    }

    @Transactional(readOnly = true)
    public Page<FriendsSearchResponse> getAllFriends(int page, Long userId) {
        Pageable pageable = PageRequest.of(page, 20, Sort.by(Sort.Order.desc("beFriendAt")).descending());
        return FriendsSearchResponse.toDto(friendRepository.findByUserId(userId, pageable));
    }

    @Transactional(readOnly = true)
    public List<User> getFriendsByNicknameContaining(Long userId, String nickname) {
        return userRepository.findFriendsByNicknameContaining(userId, nickname);
    }
}
