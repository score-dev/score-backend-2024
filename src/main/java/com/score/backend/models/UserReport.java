package com.score.backend.models;

import com.score.backend.models.enums.UserReportReason;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
public class UserReport {
    @Id
    @GeneratedValue
    @Column(name = "user_report_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User reportAgent; // 신고를 한 유저

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User reportObject; // 신고를 당한 유저

    @Enumerated(EnumType.STRING)
    private UserReportReason reason; // 신고 이유

    private String comment; // '기타' 선택 시 입력한 내용
}
