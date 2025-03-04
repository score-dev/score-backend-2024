package com.score.backend.domain.exercise.repositories;

import com.score.backend.domain.exercise.TaggedUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaggedUserRepository extends JpaRepository<TaggedUser, Long> {
}
