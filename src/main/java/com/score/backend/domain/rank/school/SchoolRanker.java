package com.score.backend.domain.rank.school;

import com.score.backend.domain.group.GroupEntity;
import com.score.backend.domain.rank.Ranker;
import com.score.backend.domain.rank.Ranking;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
@DiscriminatorValue("S")
public class SchoolRanker extends Ranker {
    @ManyToOne
    @JoinColumn(name = "group_id")
    private GroupEntity group;

    @Column(name = "participate_ratio")
    private double participateRatio;

    @Column(name = "total_exercise_time")
    private double totalExerciseTime;

    public SchoolRanker(double participateRatio, double totalExerciseTime, int rankNum, int changeAmount) {
        super(rankNum, changeAmount);
        this.totalExerciseTime = totalExerciseTime;
        this.participateRatio = participateRatio;
    }
}
