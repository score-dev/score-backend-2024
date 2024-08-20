package com.score.backend.repositories.user;

import com.score.backend.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserRepositoryCustom {
    Page<User> findFriendsPage(long userId, Pageable pageable);
    List<User> findFriendsByNicknameContaining(long userId, String nickname);
}
