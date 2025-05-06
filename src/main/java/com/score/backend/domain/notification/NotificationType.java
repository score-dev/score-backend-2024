package com.score.backend.domain.notification;

import lombok.Getter;

@Getter
public enum NotificationType {
    GOAL("목표한 운동 시간이 되었어요!", "오늘도 스코어와 함께 운동을 시작해요!"),
    JOIN_REQUEST("%s님이 %s 그룹의 메이트가 되고 싶어합니다. 수락하시겠나요?", ""),
    JOIN_COMPLETE("%s 그룹에 %s님의 가입이 완료되었습니다!", "앞으로 %s 그룹과 함께 열심히 달려봐요!"),
    BATON("%s님이 바통을 넘겼습니다!", ""),
    GROUP_RANKING("%s님이 %s 그룹에서 1등을 달리고 있습니다!", "계속 유지해보세요!"),
    SCHOOL_RANKING("%s 그룹이 %s에서 이번주 1위를 달성했어요!", "메이트 모두에게 800pt를 드립니다!"),
    TAGGED("%s님에게 함께 운동한 사람으로 태그되었어요!", "피드를 확인해보러 갈까요?"),
    ETC("", "");

    private final String title;
    private final String body;

    NotificationType(String title, String body) {
        this.title = title;
        this.body = body;
    }
}
