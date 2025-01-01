package com.score.backend.domain.rank.school;

import com.score.backend.domain.rank.Ranking;
import com.score.backend.domain.school.School;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@DiscriminatorValue("S")
@NoArgsConstructor
public class SchoolRanking extends Ranking {
    @ManyToOne
    @JoinColumn(name = "school_id")
    private School school;

    @OneToMany(mappedBy = "belongsTo")
    private List<SchoolRanker> schoolRankers = new ArrayList<>();

    public SchoolRanking(LocalDate startDate, LocalDate endDate, School school) {
        super(startDate, endDate);
        this.school = school;
    }
}
