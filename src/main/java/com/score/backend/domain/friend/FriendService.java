package com.score.backend.domain.friend;

import com.score.backend.domain.user.User;
import com.score.backend.domain.user.repositories.UserRepository;
import com.score.backend.dtos.FriendsSearchResponse;
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
    private final UserRepository userRepository;

    public void saveNewFriend(Long userId1, Long userId2) throws BadRequestException {
        User user1 = userRepository.findById(userId1).orElseThrow(
                () -> new NoSuchElementException("요청한 유저의 정보를 찾을 수 없습니다."));
        User user2 = userRepository.findById(userId2).orElseThrow(
                () -> new NoSuchElementException("친구로 추가하려는 유저의 정보를 찾을 수 없습니다."));
        if (friendRepository.findByUserIdAndFriendId(userId1, userId2).isEmpty()) {
            user1.addFriend(user2);
        } else {
            throw new BadRequestException("두 유저는 이미 친구로 등록되어 있습니다.");
        }
    }

    public void deleteFriend(Long userId1, Long userId2) {
        Friend user1 = friendRepository.findByUserIdAndFriendId(userId1, userId2).orElseThrow(
                () -> new NoSuchElementException("요청한 유저의 정보를 찾을 수 없습니다.")
        );
        Friend user2 = friendRepository.findByUserIdAndFriendId(userId2, userId1).orElseThrow(
                () -> new NoSuchElementException("삭제하려는 유저의 정보를 찾을 수 없습니다.")
        );
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
