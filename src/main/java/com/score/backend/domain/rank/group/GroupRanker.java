package com.score.backend.domain.rank.group;

import com.score.backend.domain.rank.Ranker;
import com.score.backend.domain.user.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@RequiredArgsConstructor
public class GroupRanker extends Ranker {

    @ManyToOne
    @JoinColumn(name = "ranking_id")
    @Setter
    private GroupRanking belongsTo;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "weekly_level_increment")
    private int weeklyLevelIncrement;

    @Column(name = "weekly_exercise_time")
    private double weeklyExerciseTime;

    public GroupRanker(User user, int rankNum, int changedAmount, int weeklyLevelIncrement, double weeklyExerciseTime) {
        super(rankNum, changedAmount);
        this.user = user;
        this.weeklyLevelIncrement = weeklyLevelIncrement;
        this.weeklyExerciseTime = weeklyExerciseTime;
    }
}
