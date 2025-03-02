package com.score.backend.exceptions;

import com.google.firebase.auth.FirebaseAuthException;
import com.score.backend.dtos.ErrorResponse;
import io.jsonwebtoken.JwtException;
import org.apache.tomcat.websocket.AuthenticationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import java.sql.SQLException;
import java.text.ParseException;

import static org.springframework.http.HttpStatus.*;

@ControllerAdvice
public class CustomControllerAdvice {
    @ResponseStatus(INTERNAL_SERVER_ERROR)
    @ExceptionHandler({RuntimeException.class, SQLException.class, FirebaseAuthException.class})
    public ResponseEntity<ErrorResponse> handleServerErrors(Exception ex) {
        return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ErrorResponse(INTERNAL_SERVER_ERROR.value(), ex.toString(), ex.getMessage()));
    }

    @ResponseStatus(NOT_FOUND)
    @ExceptionHandler({NotFoundException.class})
    public ResponseEntity<ErrorResponse> handleNoSuchElementException(NotFoundException ex) {
        return ResponseEntity.status(NOT_FOUND).body(new ErrorResponse(NOT_FOUND.value(), ex.toString(), ex.getType().getMessage()));
    }

    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler(ScoreCustomException.class)
    public ResponseEntity<ErrorResponse> handleBadRequestException(ScoreCustomException ex) {
        return ResponseEntity.status(BAD_REQUEST).body(new ErrorResponse(BAD_REQUEST.value(), ex.toString(), ex.getType().getMessage()));
    }

    @ResponseStatus(PAYLOAD_TOO_LARGE)
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<ErrorResponse> handleMaxUploadSizeExceededException(MaxUploadSizeExceededException ex) {
        return ResponseEntity.status(PAYLOAD_TOO_LARGE).body(new ErrorResponse(PAYLOAD_TOO_LARGE.value(), ex.toString(), ExceptionType.EXCEEDED_FILE_SIZE.getMessage()));
    }

    @ResponseStatus(UNAUTHORIZED)
    @ExceptionHandler({ParseException.class, JwtException.class, AuthenticationException.class})
    public ResponseEntity<ErrorResponse> handleAuthenticationException(Exception ex) {
        return ResponseEntity.status(UNAUTHORIZED).body(new ErrorResponse(UNAUTHORIZED.value(), ex.toString(), ex.getMessage()));
    }
}
