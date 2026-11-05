package com.eduaircontrol.msusermanagement.infrastructure.web;

import com.eduaircontrol.msusermanagement.shared.exception.ConflictException;
import com.eduaircontrol.msusermanagement.shared.exception.NotFoundException;
import com.eduaircontrol.msusermanagement.shared.exception.ValidationException;
import com.eduaircontrol.msusermanagement.infrastructure.web.dto.ErrorResponse;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(NotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ErrorResponse.of("NOT_FOUND", ex.getMessage()));
    }

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<ErrorResponse> handleConflict(ConflictException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(ErrorResponse.of("CONFLICT", ex.getMessage()));
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ErrorResponse> handleDomainValidation(ValidationException ex) {
        return ResponseEntity.badRequest()
                .body(ErrorResponse.of("VALIDATION_ERROR", ex.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException ex) {
        List<ErrorResponse.Details> details = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> new ErrorResponse.Details(error.getField(), error.getDefaultMessage()))
                .toList();
        String message = details.isEmpty()
                ? "Invalid input data"
                : details.get(0).field() + ": " + details.get(0).message();
        return ResponseEntity.badRequest().body(new ErrorResponse("VALIDATION_ERROR", message, details, null));
    }

    @ExceptionHandler({HttpMessageNotReadableException.class, MethodArgumentTypeMismatchException.class})
    public ResponseEntity<ErrorResponse> handleUnreadable(Exception ex) {
        return ResponseEntity.badRequest()
                .body(ErrorResponse.of("VALIDATION_ERROR", "Malformed or invalid request data"));
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handleDataIntegrity(DataIntegrityViolationException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(ErrorResponse.of("CONFLICT", "The operation violates a data constraint"));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleUnexpected(Exception ex) {
        log.error("Unexpected error", ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ErrorResponse.of("INTERNAL_ERROR", "Internal server error"));
    }
}
