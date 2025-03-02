package com.score.backend.exceptions;

import lombok.Getter;

@Getter
public class ScoreCustomException extends RuntimeException {
    private final ExceptionType type;
    public ScoreCustomException(ExceptionType type) {
        this.type = type;
    }
}
