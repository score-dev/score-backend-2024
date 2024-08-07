package com.score.backend.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.score.backend.config.BaseEntity;
import com.score.backend.models.enums.Gender;
import com.score.backend.models.exercise.Exercise;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcType;
import org.hibernate.type.descriptor.jdbc.SmallIntJdbcType;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "user_id")
    private Long id;

    @Column(nullable = false, unique = true)
    @Setter
    private String nickname;

    @ManyToOne
    @JoinColumn(name = "school_id")
    private School school;

    @JdbcType(value = SmallIntJdbcType.class)
    private Gender gender;

    @Setter
    private int grade;

    @Setter
    private int height;

    @Setter
    private int weight;

    @Setter
    private String profileImg;

    @Setter
    private LocalTime goal;

    private int level;

    private int point;

    private int weekLevelIncrement; // 한 주간의 레벨 상승 횟수

    private LocalDateTime lastExerciseDateTime;

    @Column(nullable = false)
    private int consecutiveDate; // 며칠 연속으로 운동 중인지?

    @Column(nullable = false)
    private double cumulativeTime; // 누적 운동 시간

    @Column(nullable = false)
    private double cumulativeDistance; // 누적 운동 거리

    @OneToMany(mappedBy="agent")
    @JsonIgnore
    private final List<Exercise> feeds = new ArrayList<>();

    @OneToMany
    private final List<User> friends = new ArrayList<>();

    @OneToMany
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private final List<User> mates = new ArrayList<>();

    @OneToMany
    @JsonIgnore
    private final List<User> blockedUsers = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "user_group",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "group_id")
    )
    @JsonIgnore
    private List<Group> groups = new ArrayList<>();

    private boolean marketing;

    private boolean push;

    private String refreshToken;

    private String loginKey;

    public void addGroup(Group group) {
        this.groups.add(group);
        group.getMembers().add(this);
    }

    public void removeGroup(Group group) {
        this.groups.remove(group);
        group.getMembers().remove(this);
    }

    public void updateCumulativeTime(double duration) {
        this.cumulativeTime += duration;
    }
    public void updateCumulativeDistance(double distance) {
        this.cumulativeDistance += distance;
    }
    public void updatePoint(int point) {
        this.point += point;
    }
    public void initPoint(int point) {
        this.point = point;
    }
    public void updateConsecutiveDate(boolean isIncrement) {
        if (isIncrement) {
            this.consecutiveDate++;
        } else {
            this.consecutiveDate = 0;
        }
    }
    public void updateLastExerciseDateTime(LocalDateTime lastExerciseDateTime) {this.lastExerciseDateTime = lastExerciseDateTime;}
    public void increaseLevel(int amount) {
        this.level = this.level + amount;
        this.weekLevelIncrement = this.weekLevelIncrement + amount;
    }
    public void setProfileImageUrl(String profileImg) {
        this.profileImg = profileImg;
    }


    public void addFriend(User user) {
        this.friends.add(user);
        user.getFriends().add(this);
    }
    public void deleteFriend(User user) {
        this.friends.remove(user);
        user.getFriends().remove(this);
    }
    public void blockUser(User user) {
        this.blockedUsers.add(user);
        user.getBlockedUsers().add(this);
    }


    public void setSchoolAndStudent(School school) {
        this.school = school;
        school.getStudents().add(this);
    }
}