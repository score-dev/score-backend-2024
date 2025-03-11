package com.score.backend.domain.friend.block;

import com.score.backend.domain.user.User;
import com.score.backend.domain.user.UserService;
import com.score.backend.domain.user.repositories.UserRepository;
import com.score.backend.dtos.FriendsSearchResponse;
import com.score.backend.exceptions.ExceptionType;
import com.score.backend.exceptions.ScoreCustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class BlockService {
    private final UserRepository userRepository;
    private final UserService userService;
    private final BlockedUserRepository blockedUserRepository;

    public void blockFriend(Long blockerId, Long blockedId) {
        User blocker = userService.findUserById(blockerId);
        User blocked = userService.findUserById(blockedId);
        if (blockedUserRepository.findByBlockerIdAndBlockedId(blockerId, blockedId).isPresent()) {
            throw new ScoreCustomException(ExceptionType.ALREADY_BLOCKED);
        }
        BlockedUser blockedUser = new BlockedUser(blocker, blocked);
        blocker.blockUser(blockedUser);
        blockedUserRepository.save(blockedUser);
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
        User blocker = userService.findUserById(blockerId);
        blocker.unblockUser(blockedUserRepository.findByBlockedId(blockedId));
    }
}
