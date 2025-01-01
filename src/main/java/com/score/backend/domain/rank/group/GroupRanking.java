package com.score.backend.domain.rank.group;

import com.score.backend.domain.group.GroupEntity;
import com.score.backend.domain.rank.Ranking;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@RequiredArgsConstructor
public class GroupRanking extends Ranking {
    @ManyToOne
    @JoinColumn(name = "group_entity_id")
    private GroupEntity group;

    @OneToMany(mappedBy = "belongsTo")
    private List<GroupRanker> groupRankers = new ArrayList<>();

    public GroupRanking(LocalDate startDate, LocalDate endDate, GroupEntity group) {
        super(startDate, endDate);
        this.group = group;
    }
}
