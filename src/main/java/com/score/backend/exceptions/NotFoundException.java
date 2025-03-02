package com.score.backend.exceptions;

import lombok.Getter;

@Getter
public class NotFoundException extends ScoreCustomException {
  public NotFoundException(ExceptionType type) {
    super(type);
  }
}
