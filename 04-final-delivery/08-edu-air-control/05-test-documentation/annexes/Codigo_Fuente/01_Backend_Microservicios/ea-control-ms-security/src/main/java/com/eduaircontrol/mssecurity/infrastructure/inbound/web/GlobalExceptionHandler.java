package com.eduaircontrol.mssecurity.infrastructure.inbound.web;

import com.eduaircontrol.mssecurity.domain.exception.AccountLockedException;
import com.eduaircontrol.mssecurity.domain.exception.ConflictException;
import com.eduaircontrol.mssecurity.domain.exception.UnauthorizedException;
import com.eduaircontrol.mssecurity.domain.exception.NotFoundException;
import com.eduaircontrol.mssecurity.domain.exception.ValidationException;
import com.eduaircontrol.mssecurity.infrastructure.inbound.web.dto.ErrorResponse;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
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
                .body(ErrorResponse.of(ex.getCode(), ex.getMessage()));
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ErrorResponse> handleUnauthorized(UnauthorizedException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(ErrorResponse.of(ex.getCode(), ex.getMessage()));
    }

    @ExceptionHandler(AccountLockedException.class)
    public ResponseEntity<ErrorResponse> handleAccountLocked(AccountLockedException ex) {
        return ResponseEntity.status(HttpStatus.LOCKED)
                .body(ErrorResponse.of("ACCOUNT_LOCKED", ex.getMessage()));
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

    /**
     * Metodo HTTP no soportado por el endpoint (p.ej. GET sobre algo que solo
     * acepta POST). Sin este manejador caia en el generico y devolvia 500.
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ErrorResponse> handleMethodNotSupported(
            HttpRequestMethodNotSupportedException ex) {
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED)
                .body(ErrorResponse.of("METHOD_NOT_ALLOWED",
                        "Method " + ex.getMethod() + " is not supported for this endpoint"));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleUnexpected(Exception ex) {
        log.error("Unexpected error", ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ErrorResponse.of("INTERNAL_ERROR", "Internal server error"));
    }
}
