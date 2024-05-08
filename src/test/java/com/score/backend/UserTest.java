package com.score.backend;

import com.score.backend.models.User;
import com.score.backend.services.UserService;
import jakarta.transaction.Transactional;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@Transactional
@ContextConfiguration(classes = BackendApplication.class)
public class UserTest {

    @Autowired
    private UserService userService;

    @Test
    public void findByNicknameTest() {
        Optional<User> testid1 = userService.findUserByNickname("testid1");
        assertThat(testid1.isPresent()).isTrue();
        assertThat(testid1.get().getNickname()).isEqualTo("testid1");
    }
}
