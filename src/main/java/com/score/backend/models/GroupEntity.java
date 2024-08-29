package com.score.backend.models;

import com.score.backend.models.grouprank.GroupRanking;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import com.score.backend.config.BaseEntity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Entity
@Table(name = "group_entity")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GroupEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "groupId")
    private Long groupId;

    @ManyToOne
    @JoinColumn(name = "school_id")
    private School belongingSchool;

    @Column(name = "groupName", nullable = false, unique = true, length = 15)
    private String groupName;

    @Column(name = "groupDescription", nullable = false, length = 200)
    private String groupDescription;

    @Column(name = "groupImg", nullable = false)
    private String groupImg;

    @Column(name = "isPrivate", nullable = false)
    private boolean isPrivate;

    @Column(name = "groupPassword", length = 4)
    private String groupPassword;

    @Column(name = "userLimit", length = 50)
    private int userLimit;

    @CreatedDate
    private LocalDateTime groupCreatedAt;

    @LastModifiedDate
    private LocalDateTime groupUpdatedAt;

    @ManyToOne
    @JoinColumn(name="admin_id", nullable=false)
    private User admin;

    @ManyToMany(mappedBy = "groups")
    @Builder.Default
    private Set<User> members = new HashSet<>(); //회원들과의 관계

    @OneToMany(mappedBy = "group")
    @Builder.Default
    private List<GroupRanking> groupRankings = new ArrayList<>();

    @Column(name = "cumulativeTime")
    private double cumulativeTime; // 그룹의 누적 운동 시간 (단위: 초)

    @Column(name = "todayExercisedCount")
    private int todayExercisedCount; // 오늘 운동한 메이트 수

    public void updateCumulativeTime(double duration) {
        this.cumulativeTime += duration;
    }
    public void increaseTodayExercisedCount() {
        this.todayExercisedCount++;
    }
    public void initTodayExercisedCount() {
        this.todayExercisedCount = 0;
    }
}
