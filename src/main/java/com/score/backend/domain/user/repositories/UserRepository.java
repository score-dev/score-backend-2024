package com.score.backend.domain.user.repositories;

import com.score.backend.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long>, UserRepositoryCustom {
    @Query("select u from User u where u.nickname = :nickname")
    Optional<User> findByNickname(@Param("nickname") String nickname);

    @Query("select u from User u where u.loginKey = :key")
    Optional<User> findByKey(@Param("key") Long loginKey);

    List<User> findAllByGoal(LocalTime goal);
}