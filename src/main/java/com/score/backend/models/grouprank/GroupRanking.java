package com.score.backend.models.grouprank;

import com.score.backend.models.Group;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@RequiredArgsConstructor
public class GroupRanking {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "group_ranking_id")
    private Long id;

    @Column(name="start_date")
    private LocalDate startDate;

    @Column(name="end_date")
    private LocalDate endDate;

    @ManyToOne
    @JoinColumn(name = "group_id")
    private Group group;

    @OneToMany(mappedBy = "belongingRanking")
    private List<GroupRanker> groupRankers = new ArrayList<>();

    public GroupRanking(LocalDate startDate, LocalDate endDate, Group group) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.group = group;
    }
}
