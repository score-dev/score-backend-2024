package com.score.backend.models.grouprank;

import com.score.backend.models.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@RequiredArgsConstructor
public class GroupRanker {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "group_ranker_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "group_ranking_id")
    @Setter
    private GroupRanking belongingRanking;

    @Column(name = "ranking")
    @Setter
    private int ranking;

    @Column(name = "weekly_level_increment")
    private int weeklyLevelIncrement;

    @Column(name = "weekly_exercise_time")
    private double weeklyExerciseTime;

    @Column(name = "changed_degree")
    @Setter
    private int changedDegree;

    public GroupRanker(User user, int weeklyLevelIncrement, double weeklyExerciseTime) {
        this.user = user;
        this.weeklyLevelIncrement = weeklyLevelIncrement;
        this.weeklyExerciseTime = weeklyExerciseTime;
    }
}
