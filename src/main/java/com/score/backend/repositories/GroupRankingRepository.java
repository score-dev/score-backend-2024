package com.score.backend.repositories;

import com.score.backend.models.grouprank.GroupRanking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface GroupRankingRepository extends JpaRepository<GroupRanking, Long> {
    @Query("select gr from GroupRanking gr where gr.group.groupId = :groupId and gr.startDate = :date")
    GroupRanking findByGroupIdAndDate(@Param("groupId") Long groupId, @Param("date") LocalDate localDate);
}
