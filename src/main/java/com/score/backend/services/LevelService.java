package com.score.backend.services;

import com.score.backend.models.User;
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
        User user = userService.findUserById(userId).orElseThrow(
                () -> new RuntimeException("User not found")
        );
        double currDistance = user.getCumulativeDistance();
        user.updatePoint((int)(currDistance % 10 + newDistance) / 10 * 100);
        user.updatePoint((int)(currDistance % 30 + newDistance) / 30 * 300);
    }

    // 연속 운동 일수에 따른 포인트 증가
    public void increasePointsByConsecutiveDate(Long userId) {
        User user = userService.findUserById(userId).orElseThrow(
                () -> new RuntimeException("User not found")
        );
        switch (user.getConsecutiveDate() + 1) {
            case 3, 4, 5, 6: user.updatePoint(100); break;
            case 7, 8, 9, 10, 11, 12, 13, 14: user.updatePoint(300); break;
            case 15: user.updatePoint(500); break;
            default: break;
        }
    }
}
