package com.score.backend.domain.rank.group;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface GroupRankerRepository extends JpaRepository<GroupRanker, Long>, GroupRankerRepositoryCustom {
    @Query("select gr from GroupRanker gr where gr.belongsTo.id = :groupRankingId and gr.user.id = :userId")
    GroupRanker findByGroupRankingIdAndUserId(@Param("groupRankingId") Long groupRankingId, @Param("userId") Long userId);
}
