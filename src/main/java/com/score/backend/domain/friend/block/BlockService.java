package com.score.backend.domain.friend.block;

import com.score.backend.domain.user.User;
import com.score.backend.domain.user.repositories.UserRepository;
import com.score.backend.dtos.FriendsSearchResponse;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
@Transactional
public class BlockService {
    private final UserRepository userRepository;
    private final BlockedUserRepository blockedUserRepository;

    public void blockFriend(Long blockerId, Long blockedId) throws BadRequestException {
        User blocker = userRepository.findById(blockerId).orElseThrow(
                () -> new NoSuchElementException("요청한 유저를 찾을 수 없습니다."));
        User blocked = userRepository.findById(blockedId).orElseThrow(
                () -> new NoSuchElementException("차단하려는 유저를 찾을 수 없습니다."));
        blocker.blockUser(new BlockedUser(blocker, blocked));
    }

    public List<FriendsSearchResponse> findBlockedFriends(Long blockerId) {
        List<BlockedUser> blockedUsers = blockedUserRepository.findByBlockerId(blockerId);
        List<FriendsSearchResponse> blockedDto = new ArrayList<>();
        for (BlockedUser blockedUser : blockedUsers) {
            User user = blockedUser.getBlocked();
            blockedDto.add(new FriendsSearchResponse(user.getId(), user.getNickname(), user.getProfileImg()));
        }
        return blockedDto;
    }

    public void unblockFriend(Long blockerId, Long blockedId) {
        User blocker = userRepository.findById(blockerId).orElseThrow(
                () -> new NoSuchElementException("요청한 유저를 찾을 수 없습니다.")
        );
        blocker.unblockUser(blockedUserRepository.findByBlockedId(blockedId));
    }
}