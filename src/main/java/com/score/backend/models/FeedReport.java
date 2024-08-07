package com.score.backend.models;

import com.score.backend.models.enums.FeedReportReason;
import com.score.backend.models.exercise.Exercise;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class FeedReport {
    @Id
    @GeneratedValue
    @Column(name="feed_report_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User reportAgent; // 신고를 한 유저

    @ManyToOne
    @JoinColumn(name = "exercise_id")
    private Exercise reportedFeed; // 신고한 피드

    @Enumerated(EnumType.STRING)
    private FeedReportReason reason; // 신고 이유

    private String comment; // 신고 이유로 '기타' 선택 시 입력한 내용
}
