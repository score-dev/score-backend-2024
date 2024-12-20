package com.score.backend.domain.report.userreport;

import com.score.backend.domain.user.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class UserReport {
    @Id
    @GeneratedValue
    @Column(name = "user_report_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "agent_id")
    private User reportAgent; // 신고를 한 유저

    @ManyToOne
    @JoinColumn(name = "object_id")
    private User reportObject; // 신고를 당한 유저

    @Enumerated(EnumType.STRING)
    private UserReportReason reason; // 신고 이유

    private String comment; // '기타' 선택 시 입력한 내용
}
