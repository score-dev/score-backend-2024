package com.score.backend.domain.rank.school;

import com.score.backend.domain.group.GroupEntity;
import com.score.backend.domain.rank.Ranker;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@DiscriminatorValue("S")
public class SchoolRanker extends Ranker {
    @ManyToOne
    @Setter
    private SchoolRanking belongsTo;

    @ManyToOne
    private GroupEntity group;

    @Column(name = "participate_ratio")
    private double participateRatio;

    @Column(name = "total_exercise_time")
    private double totalExerciseTime;

    public SchoolRanker(GroupEntity group, double participateRatio, double totalExerciseTime, int rankNum, int changeAmount) {
        super(rankNum, changeAmount);
        this.group = group;
        this.totalExerciseTime = totalExerciseTime;
        this.participateRatio = participateRatio;
    }
}
