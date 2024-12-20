package com.score.backend.domain.group.rank;

import com.score.backend.domain.group.GroupEntity;
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
    @JoinColumn(name = "group_entity_id")
    private GroupEntity group;

    @OneToMany(mappedBy = "belongingRanking")
    private List<GroupRanker> groupRankers = new ArrayList<>();

    public GroupRanking(LocalDate startDate, LocalDate endDate, GroupEntity group) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.group = group;
    }
}
