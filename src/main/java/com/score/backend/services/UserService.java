package com.score.backend.services;

import com.score.backend.models.User;
import com.score.backend.models.dtos.UserUpdateDto;
import com.score.backend.repositories.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final ImageUploadService imageUploadService;

    @Transactional
    public void saveUser(User user, MultipartFile profileImage) {
        // db에 기본 프로필 이미지 저장된 후 프로필 사진 미설정시 기본 프로필 이미지 설정되도록 하는 기능 구현 필요
        user.setProfileImageUrl(imageUploadService.uploadImage(profileImage));
        userRepository.save(user);
    }

    @Transactional
    public void updateUser(Long userId, UserUpdateDto userUpdateDto , MultipartFile profileImage) {
        User user = this.findUserById(userId).orElseThrow(null); // 예외 처리 필요
        user.setProfileImageUrl(imageUploadService.uploadImage(profileImage));
        user.setGoal(userUpdateDto.getGoal());
        user.setHeight(userUpdateDto.getHeight());
        user.setWeight(userUpdateDto.getWeight());
        user.setGrade(userUpdateDto.getGrade());
        if (!user.getSchool().getSchoolCode().equals(userUpdateDto.getSchool().getSchoolCode())) {
            user.setSchoolAndStudent(userUpdateDto.getSchool());
        }
    }

    @Transactional
    public void withdrawUser(Long id) {
        User deletingUser = findUserById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found")
        ); // 예외 처리 필요
        userRepository.delete(deletingUser);
    }

    public Optional<User> findUserById(Long id) {
        return userRepository.findById(id);
    }

    public List<User> findUsersByGoal(LocalTime curr) {
        return userRepository.findAllByGoal(curr);
    }

    public List<User> findAll() {
        return userRepository.findAll();
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
