package com.score.backend.repositories.user;

import com.score.backend.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserRepositoryCustom {
    Page<User> findFriendsPage(long userId, Pageable pageable);
}
