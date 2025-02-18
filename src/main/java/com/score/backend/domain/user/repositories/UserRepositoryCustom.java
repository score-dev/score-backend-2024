package com.score.backend.domain.user.repositories;

import com.score.backend.domain.friend.Friend;
import com.score.backend.domain.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserRepositoryCustom {
    List<Friend> findFriendsByNicknameContaining(Long userId, String nickname);
    List<User> findGroupMatesWhoDidNotExerciseToday(Long groupId);
    List<User> findGroupMatesWhoDidExerciseToday(Long groupId);
}
