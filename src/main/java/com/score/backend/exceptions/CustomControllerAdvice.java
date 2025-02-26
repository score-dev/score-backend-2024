package com.score.backend.exceptions;

import com.google.firebase.auth.FirebaseAuthException;
import com.score.backend.dtos.ErrorResponse;
import org.apache.coyote.BadRequestException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.sql.SQLException;
import java.util.NoSuchElementException;

import static org.springframework.http.HttpStatus.*;

@ControllerAdvice
public class CustomControllerAdvice {
    @ResponseStatus(INTERNAL_SERVER_ERROR)
    @ExceptionHandler({RuntimeException.class, SQLException.class, FirebaseAuthException.class})
    public ResponseEntity<ErrorResponse> handleServerErrors(Exception ex) {
        return ResponseEntity.internalServerError().body(new ErrorResponse(INTERNAL_SERVER_ERROR.value(), ex.toString(), ex.getMessage()));
    }

    @ResponseStatus(NOT_FOUND)
    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<ErrorResponse> handleNoSuchElementException(NoSuchElementException ex) {

        return ResponseEntity.status(NOT_FOUND).body(new ErrorResponse(NOT_FOUND.value(), ex.toString(), ex.getMessage()));
    }

    @ResponseStatus(CONFLICT)
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorResponse> handleBadRequestException(BadRequestException ex) {
        return ResponseEntity.status(CONFLICT).body(new ErrorResponse(CONFLICT.value(), ex.toString(), ex.getMessage()));
    }
}
