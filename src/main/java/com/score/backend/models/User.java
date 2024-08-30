package com.score.backend.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.score.backend.config.BaseEntity;
import com.score.backend.models.dtos.NotificationStatusRequest;
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

    private LocalDateTime lastExerciseDateTime;

    @Column(nullable = false)
    private int consecutiveDate; // 며칠 연속으로 운동 중인지?

    private double weeklyCumulativeTime; // 한 주간의 누적 운동 시간

    private int weeklyExerciseCount; // 한 주간 운동한 날짜 수

    private int weeklyLevelIncrement; // 한 주간 레벨 상승 횟수

    @Column(nullable = false)
    private double totalCumulativeTime; // 누적 운동 시간

    @Column(nullable = false)
    private double cumulativeDistance; // 누적 운동 거리

    @OneToMany(mappedBy="agent")
    @JsonIgnore
    private final List<Exercise> feeds = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "user_friends",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "friend_id")
    )
    @JsonIgnore
    private final List<User> friends = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "user_mates",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "mate_id")
    )
    @JsonIgnore
    private final List<User> mates = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "user_blocked_users",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "blocked_user_id")
    )
    @JsonIgnore
    private final List<User> blockedUsers = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "user_group",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "group_id")
    )
    @JsonIgnore
    private List<GroupEntity> groups = new ArrayList<>();

    @OneToMany(mappedBy = "agent")
    @JsonIgnore
    private List<Notification> notifications = new ArrayList<>();

    private boolean marketing; // 마케팅 알림 수신 동의 여부

    private boolean exercisingTime; // 목표 운동 시간 알림 수신 동의 여부

    private boolean tag; // 소통 알림(태그 알림) 수신 동의 여부

    private String refreshToken;

    private String loginKey;

    @Setter
    private String fcmToken;

    @Setter
    private LocalDateTime joinedAt;

    public void addGroup(GroupEntity group) {
        this.groups.add(group);
        group.getMembers().add(this);
    }

    public void removeGroup(GroupEntity group) {
        this.groups.remove(group);
        group.getMembers().remove(this);
    }

    public void updateCumulativeTime(double duration) {
        this.totalCumulativeTime += duration;
    }
    public void updateCumulativeDistance(double distance) {
        this.cumulativeDistance += distance;
    }
    public void updatePoint(int point) {
        this.point += point;
        // 500 포인트 달성 시 레벨업 + 포인트 초기화
        if (point >= 500) {
            this.increaseLevel(point / 500);
            this.initPoint(point % 500);
        }
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
        this.weeklyLevelIncrement = this.weeklyLevelIncrement + amount;
    }
    public void initWeeklyExerciseStatus() {
        this.weeklyLevelIncrement = 0;
        this.weeklyCumulativeTime = 0;
        this.weeklyExerciseCount = 0;
    }
    public void updateWeeklyExerciseStatus(boolean needToIncreaseCount, double duration) {
        if (needToIncreaseCount) {
            this.weeklyExerciseCount++;
        }
        this.weeklyCumulativeTime += duration;
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

    public void setNotificationReceivingStatus(NotificationStatusRequest request) {
        this.marketing = request.isMarketing();
        this.exercisingTime = request.isExercisingTime();
        this.tag = request.isTag();
    }
}