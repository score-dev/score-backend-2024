package com.score.backend.domain.rank.school;

import com.score.backend.domain.rank.Ranking;
import com.score.backend.domain.school.School;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;

@Entity
@Getter
@DiscriminatorValue("S")
public class SchoolRanking extends Ranking {
    @ManyToOne
    @JoinColumn(name = "school_id")
    private School school;
}
