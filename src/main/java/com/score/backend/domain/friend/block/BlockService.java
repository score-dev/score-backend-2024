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
    private final BlockedUserRepository blockedUserRepository;

    public void blockFriend(User blocker, User blocked) {
        if (blockedUserRepository.findByBlockerIdAndBlockedId(blocker.getId(), blocked.getId()).isPresent()) {
            throw new ScoreCustomException(ExceptionType.ALREADY_BLOCKED);
        }
        BlockedUser blockedUser = new BlockedUser(blocker, blocked);
        blocker.blockUser(blockedUser);
        blockedUserRepository.save(blockedUser);
    }

    public List<FriendsSearchResponse> findBlockedFriends(User blocker) {
        List<BlockedUser> blockedUsers = blocker.getBlockedUsers();
        List<FriendsSearchResponse> blockedDto = new ArrayList<>();
        for (BlockedUser blockedUser : blockedUsers) {
            User user = blockedUser.getBlocked();
            blockedDto.add(new FriendsSearchResponse(user.getId(), user.getNickname(), user.getProfileImg()));
        }
        return blockedDto;
    }

    public void unblockFriend(User blocker, User blocked) {
        blocker.unblockUser(blockedUserRepository.findByBlockedId(blocked.getId()));
    }
}
