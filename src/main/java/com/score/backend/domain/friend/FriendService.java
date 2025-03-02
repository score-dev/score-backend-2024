package com.score.backend.domain.friend;

import com.score.backend.domain.user.User;
import com.score.backend.domain.user.UserService;
import com.score.backend.domain.user.repositories.UserRepository;
import com.score.backend.dtos.FriendsSearchResponse;
import com.score.backend.exceptions.ExceptionType;
import com.score.backend.exceptions.NotFoundException;
import com.score.backend.exceptions.ScoreCustomException;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
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
    private final UserService userService;
    private final UserRepository userRepository;

    public Friend findFriendByEachUsersId(Long userId1, Long userId2) {
        return friendRepository.findByUserIdAndFriendId(userId1, userId2).orElseThrow(
                () -> new NotFoundException(ExceptionType.USER_NOT_FOUND));
    }

    public void saveNewFriend(Long userId1, Long userId2) {
        User user1 = userService.findUserById(userId1);
        User user2 = userService.findUserById(userId2);
        if (friendRepository.findByUserIdAndFriendId(userId1, userId2).isEmpty()) {
            user1.addFriend(user2);
        } else {
            throw new ScoreCustomException(ExceptionType.ALREADY_FRIEND);
        }
    }

    public void deleteFriend(Long userId1, Long userId2) {
        Friend user1 = findFriendByEachUsersId(userId1, userId2);
        Friend user2 = findFriendByEachUsersId(userId2, userId1);
        user1.getUser().deleteFriend(user1, user2);
        user2.getUser().deleteFriend(user1, user2);
    }

    @Transactional(readOnly = true)
    public Page<FriendsSearchResponse> getAllFriends(int page, Long userId) {
        Pageable pageable = PageRequest.of(page, 20, Sort.by(Sort.Order.desc("createdAt")).descending());
        Page<User> userPages = friendRepository.findByUserId(userId, pageable);
        return FriendsSearchResponse.toDto(userPages);
    }

    @Transactional(readOnly = true)
    public List<Friend> getFriendsByNicknameContaining(Long userId, String nickname) {
        return userRepository.findFriendsByNicknameContaining(userId, nickname);
    }
}
