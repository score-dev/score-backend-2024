package com.score.backend.domain.friend;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FriendRepository extends JpaRepository<Friend, Long> {
    @Query("select f from Friend f where f.user.id = :userId and f.friend.id = :friendId")
    Optional<Friend> findByUserIdAndFriendId(@Param("userId") Long userId, @Param("friendId") Long friendId);
    Page<Friend> findByUserId(Long userId, Pageable pageable);
}
