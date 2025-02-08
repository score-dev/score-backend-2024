package com.score.backend.domain.rank;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn
@NoArgsConstructor
public abstract class Ranker {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ranker_id")
    private Long id;

    @Column(name = "rank_num")
    @Setter
    private int rankNum;

    @Column(name = "changed_amount")
    private int changedAmount;

    public Ranker(int rankNum, int changedAmount) {
        this.rankNum = rankNum;
        this.changedAmount = changedAmount;
    }
}
