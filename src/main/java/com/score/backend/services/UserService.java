package com.score.backend.services;

import com.score.backend.models.User;
import com.score.backend.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    @Transactional
    public void saveUser(User user) {
        userRepository.save(user);
    }

    public Optional<User> findUserByNickname(String nickname) {
        return userRepository.findByNickname(nickname);
    }

    public Optional<User> findUserByKey(String key) {
        return userRepository.findByKey(key);
    }

    public boolean isPresentUser(String key) {
        Optional<User> userOption = userRepository.findByKey(key);
        return userOption.isPresent();
    }
}
