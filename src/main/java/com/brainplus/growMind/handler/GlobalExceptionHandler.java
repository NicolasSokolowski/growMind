package com.brainplus.growMind.handler;

import com.brainplus.growMind.exception.ValidationException;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.naming.AuthenticationException;
import java.nio.file.AccessDeniedException;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;

@RestControllerAdvice
public class GlobalExceptionHandler {

  private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

  @ExceptionHandler(AccessDeniedException.class)
  public ResponseEntity<String> handleAccessDeniedException(AccessDeniedException exception) {
    logger.error("Access denied: ", exception);
    return ResponseEntity
        .status(HttpStatus.FORBIDDEN)
        .body("Access denied: You do not have permission to access this resource.");
  }

  @ExceptionHandler(AuthenticationException.class)
  public ResponseEntity<String> handleAuthenticationException(AuthenticationException exception) {
    logger.error("Authentication failed: ", exception);
    return ResponseEntity
        .status(HttpStatus.UNAUTHORIZED)
        .body("Authentication failed. Please verify your credentials.");
  }

  @ExceptionHandler(EmptyResultDataAccessException.class)
  public ResponseEntity<String> handleEmptyResultDataAccessException(EmptyResultDataAccessException exception) {
    logger.error("Resource not found: ", exception);
    return ResponseEntity
        .status((HttpStatus.NOT_FOUND))
        .body("The requested resource was not found.");
  }

  @ExceptionHandler(DataIntegrityViolationException.class)
  public ResponseEntity<String> handleDataIntegrityViolationException(DataIntegrityViolationException exception) {
    logger.error("Data integrity violation occurred: ", exception);
    return ResponseEntity
        .status(HttpStatus.CONFLICT)
        .body("A data integrity violation occurred. Please ensure your data is correct.");
  }

  @ExceptionHandler(MissingPathVariableException.class)
  public ResponseEntity<String> handleMissingPathVariableException(MissingPathVariableException exception) {
    logger.error("Path variable missing: ", exception);

    return ResponseEntity
        .status(HttpStatus.BAD_REQUEST)
        .body("Missing path variable: '" + exception.getVariableName() + "'. Please provide the required path variable");
  }

  @ExceptionHandler(ValidationException.class)
  public ResponseEntity<Map<String, Object>> handleValidationException(ValidationException exception) {
    logger.error("Validation errors: ", exception);

    Map<String, Object> response = new HashMap<>();
    response.put("status", HttpStatus.BAD_REQUEST.value());
    response.put("errors", exception.getViolations());
    response.put("message", "Validation failed for the request");

    return ResponseEntity
        .status(HttpStatus.BAD_REQUEST)
        .body(response);
  }

  @ExceptionHandler(IllegalStateException.class)
  public ResponseEntity<String> handleIllegalStateException(IllegalStateException exception) {
    logger.error("Illegal state occurred: ", exception);
    return ResponseEntity
        .status(HttpStatus.BAD_REQUEST)
        .body("An unexpected error occurred while processing your request. Please try again later.");
  }

}
