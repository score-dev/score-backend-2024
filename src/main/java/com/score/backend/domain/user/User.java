package com.score.backend.domain.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.score.backend.config.BaseEntity;
import com.score.backend.domain.friend.block.BlockedUser;
import com.score.backend.domain.friend.Friend;
import com.score.backend.domain.group.GroupEntity;
import com.score.backend.domain.group.UserGroup;
import com.score.backend.domain.notification.Notification;
import com.score.backend.domain.school.School;
import com.score.backend.dtos.NotificationStatusRequest;
import com.score.backend.domain.exercise.Exercise;
import jakarta.persistence.*;
import lombok.*;

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

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(nullable = false, unique = true)
    @Setter
    private String nickname;

    @ManyToOne
    @JoinColumn(name = "school_id")
    private School school;

    private LocalDateTime schoolUpdatedAt;

    @Column(columnDefinition = "varchar(255)")
    @Enumerated(EnumType.STRING)
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

    @OneToMany(mappedBy="agent", cascade = CascadeType.REMOVE, orphanRemoval = true)
    @JsonIgnore
    @Builder.Default
    private final List<Exercise> feeds = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    @Builder.Default
    private final List<Friend> friends = new ArrayList<>();

    @OneToMany(mappedBy = "blocker", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private final List<BlockedUser> blockedUsers = new ArrayList<>();

    @JsonIgnore
    @Builder.Default
    @OneToMany(mappedBy = "member", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<UserGroup> userGroups = new ArrayList<>();

    @OneToMany(mappedBy = "receiver", cascade = CascadeType.REMOVE, orphanRemoval = true)
    @JsonIgnore
    @Builder.Default
    private List<Notification> notifications = new ArrayList<>();

    private boolean marketing; // 마케팅 알림 수신 동의 여부

    private boolean exercisingTime; // 목표 운동 시간 알림 수신 동의 여부

    private boolean tag; // 소통 알림(태그 알림) 수신 동의 여부

    @Setter
    private String refreshToken;

    private String loginKey;

    @Setter
    private String fcmToken;

    @Setter
    private LocalDateTime joinedAt;

    public void addGroup(UserGroup userGroup, GroupEntity group) {
        this.userGroups.add(userGroup);
        userGroup.setMember(this);
        group.getMembers().add(userGroup);
    }

    public void updateCumulativeTime(double duration) {
        this.totalCumulativeTime += duration;
    }
    public void updateCumulativeDistance(double distance) {
        this.cumulativeDistance += distance;
    }
    public void updatePoint(int amount) {
        point += amount;
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
        this.level += amount;
        this.weeklyLevelIncrement += amount;
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
        this.friends.add(new Friend(this, user));
        user.getFriends().add(new Friend(user, this));
    }
    public void deleteFriend(Friend user, Friend friend) {
        this.friends.remove(friend);
        friend.getFriend().getFriends().remove(user);
    }
    public void blockUser(BlockedUser blocked) {
        this.blockedUsers.add(blocked);
    }
    public void unblockUser(BlockedUser blocked) {
        this.blockedUsers.remove(blocked);
    }

    public void setSchoolAndStudent(School school) {
        this.school = school;
        school.getStudents().add(this);
        this.schoolUpdatedAt = LocalDateTime.now();
    }

    public void setNotificationReceivingStatus(NotificationStatusRequest request) {
        this.marketing = request.isMarketing();
        this.exercisingTime = request.isExercisingTime();
        this.tag = request.isTag();
    }
}