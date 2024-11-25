package com.brainplus.growMind.exception;

import lombok.Getter;

import java.util.Set;

@Getter
public class ValidationException extends RuntimeException {

  private final Set<String> violations;

  public ValidationException(Set<String> violations) {
    super("Validation failed");
    this.violations = violations;
  }

}
