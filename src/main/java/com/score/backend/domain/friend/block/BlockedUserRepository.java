package com.score.backend.domain.friend.block;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface BlockedUserRepository extends JpaRepository<BlockedUser, Long> {
    List<BlockedUser> findByBlockerId(Long blockerId);
    List<BlockedUser> findByBlockedId(Long blockedId);
    @Query("select b from BlockedUser b where b.blocker.id = :blockerId and b.blocked.id = :blockedId")
    Optional<BlockedUser> findByBlockerIdAndBlockedId(@Param("blockerId") Long blockerId, @Param("blockedId") Long blockedId);
}
