package com.score.backend.domain.friend.block;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BlockedUserRepository extends JpaRepository<BlockedUser, Long> {
    List<BlockedUser> findByBlockerId(Long blockerId);
    BlockedUser findByBlockedId(Long blockedId);
}
