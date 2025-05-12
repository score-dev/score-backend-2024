package com.score.backend.domain.group;

import com.score.backend.domain.school.School;
import com.score.backend.domain.user.User;
import com.score.backend.domain.rank.group.GroupRanking;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import com.score.backend.config.BaseEntity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "group_entity")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GroupEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "group_id")
    private Long groupId;

    @ManyToOne
    @JoinColumn(name = "school_id")
    private School belongingSchool;

    @Column(name = "group_name", nullable = false, unique = true, length = 15)
    private String groupName;

    @Column(name = "group_description", nullable = false, length = 200)
    private String groupDescription;

    @Column(name = "group_img", nullable = false)
    private String groupImg;

    @Column(name = "is_private", nullable = false)
    private boolean isPrivate;

    @Column(name = "group_password", length = 4)
    private String groupPassword;

    @Column(name = "user_limit", length = 50)
    private int userLimit;

    @CreatedDate
    private LocalDateTime groupCreatedAt;

    @LastModifiedDate
    private LocalDateTime groupUpdatedAt;

    @ManyToOne
    @JoinColumn(name="admin_id", nullable=false)
    private User admin;

    @OneToMany(mappedBy = "group")
    @Builder.Default
    private List<UserGroup> members = new ArrayList<>();

    @OneToMany(mappedBy = "group")
    @Builder.Default
    private List<GroupRanking> groupRankings = new ArrayList<>();

    @Column(name = "cumulative_time")
    private double cumulativeTime; // 그룹의 누적 운동 시간 (단위: 초)

    public void updateCumulativeTime(double duration) {
        this.cumulativeTime += duration;
    }
}
