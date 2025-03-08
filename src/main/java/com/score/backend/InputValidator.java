package com.score.backend;

import com.score.backend.exceptions.ExceptionType;
import com.score.backend.exceptions.ScoreCustomException;
import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

@Component
public class InputValidator {
    public void validateNickname(String nickname) {
        if (!Pattern.matches("^[가-힣]{1,10}$", nickname)) {
            throw new ScoreCustomException(ExceptionType.NICKNAME_FORMAT_ERROR);
        }
    }
}
