package com.score.backend.domain.school;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.score.backend.config.BaseEntity;
import com.score.backend.domain.group.GroupEntity;
import com.score.backend.domain.rank.school.SchoolRanking;
import com.score.backend.domain.user.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class School extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "school_id")
    private Long id;

    private String schoolName;

    @Column(nullable = false, unique = true)
    private String schoolCode;

    @OneToMany(mappedBy="school")
    @JsonIgnore
    @Builder.Default
    private List<User> students = new ArrayList<>();

    @OneToMany(mappedBy = "belongingSchool")
    @JsonIgnore
    @Builder.Default
    private List<GroupEntity> groups = new ArrayList<>();

    @OneToMany(mappedBy = "school")
    @JsonIgnore
    @Builder.Default
    private List<SchoolRanking> schoolRankings = new ArrayList<>();
}
