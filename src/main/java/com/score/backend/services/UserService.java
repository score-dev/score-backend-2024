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

    @Transactional
    public void withdrawUser(String nickname) {
        User deletingUser = findUserByNickname(nickname).orElseThrow(null); // 예외 처리 필요
        userRepository.delete(deletingUser);
    }

    public Optional<User> findUserById(Long id) {
        return userRepository.findById(id);
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
