package com.score.backend.domain.rank.school;

import com.score.backend.domain.rank.Ranking;
import com.score.backend.domain.school.School;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Getter
@DiscriminatorValue("S")
@NoArgsConstructor
public class SchoolRanking extends Ranking {
    @ManyToOne
    @JoinColumn(name = "school_id")
    private School school;

    public SchoolRanking(LocalDate startDate, LocalDate endDate, School school) {
        super(startDate, endDate);
        this.school = school;
    }
}
