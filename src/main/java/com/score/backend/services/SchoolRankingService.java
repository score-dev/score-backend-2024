package com.score.backend.services;

import com.score.backend.models.Group;
import com.score.backend.models.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class SchoolRankingService {

    // 그룹의 참여율 계산
    public double getRatioOfParticipate(Group group) {
        Set<User> members = group.getMembers();
        if (!members.isEmpty()) {
            int sum = 0;
            for (User member : members) {
                int exerciseCount = member.getWeeklyExerciseCount();
                switch (exerciseCount) {
                    case 1: sum += 14; break;
                    case 2: sum += 28; break;
                    case 3: sum += 42; break;
                    case 4: sum += 56; break;
                    case 5: sum += 70; break;
                    case 6: sum += 84; break;
                    case 7: sum += 100; break;
                }
            }
            return  Math.round((double) sum / members.size());
        }
        return 0.0;
    }
}
