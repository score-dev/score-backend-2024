package com.score.backend.services;

import com.score.backend.models.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LevelService {

    private final UserService userService;

    @Transactional
    public void increasePointsByWalkingDistance(Long userId, double newDistance) {
        User user = userService.findUserById(userId).orElseThrow(
                () -> new RuntimeException("User not found")
        );
        double currDistance = user.getCumulativeDistance();
        user.updatePoint((int)(currDistance % 10 + newDistance) / 10 * 100);
        user.updatePoint((int)(currDistance % 30 + newDistance) / 30 * 300);
    }


}
