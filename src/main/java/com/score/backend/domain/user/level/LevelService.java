package com.score.backend.domain.user.level;

import com.score.backend.domain.user.User;
import com.score.backend.domain.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class LevelService {

    private final UserService userService;

    // 누적 운동 거리에 따른 포인트 증가
    public void increasePointsByWalkingDistance(Long userId, double newDistance) {
        User user = userService.findUserById(userId);
        double currDistance = user.getCumulativeDistance();
        user.updatePoint((int)(currDistance % 10000 + newDistance) / 10000 * 100);
        user.updatePoint((int)(currDistance % 30000 + newDistance) / 30000 * 300);
    }

    // 연속 운동 일수에 따른 포인트 증가
    public void increasePointsByConsecutiveDate(Long userId) {
        User user = userService.findUserById(userId);
        int currConsecutiveDate = user.getConsecutiveDate();
        if (currConsecutiveDate > 15) {
            currConsecutiveDate = currConsecutiveDate % 15;
        }
        switch (currConsecutiveDate) {
            case 3: user.updatePoint(100); break;
            case 7: user.updatePoint(300); break;
            case 15: user.updatePoint(500); break;
            default: break;
        }
    }

    // 10분 이상 운동했을 경우 포인트 증가
    public void increasePointsForTodaysFirstExercise(Long userId) {
        User user = userService.findUserById(userId);
        user.updatePoint(100);
    }
}
