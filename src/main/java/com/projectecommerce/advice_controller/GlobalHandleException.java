package com.projectecommerce.advice_controller;

import com.projectecommerce.model.dto.response.APIResponse;
import com.projectecommerce.utils.exception.ConflictException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.*;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.security.authentication.BadCredentialsException;


import java.nio.file.AccessDeniedException;
import java.time.LocalDateTime;
import java.util.*;


@RestControllerAdvice
public class GlobalHandleException extends ResponseEntityExceptionHandler{

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<APIResponse<?>> handleIllegalArgument(IllegalArgumentException ex) {
        Map<String, String> errors = new HashMap<>();
        errors.put("error", ex.getMessage());

        return new ResponseEntity<>(APIResponse.builder()
                .success(false)
                .message("Dữ liệu không hợp lệ")
                .errors(errors)
                .data(null)
                .timeStamp(LocalDateTime.now())
                .build(), HttpStatus.BAD_REQUEST);
    }


    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request) {

        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage()));

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(Map.of("status", 400, "message", "Validation failed", "errors", errors));
    }


    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Object> handleConstraintViolation(ConstraintViolationException ex){
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(Map.of("status", 400, "message", "Constraint violation", "errors", ex.getMessage()));
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<Object> handleNotFound(NoSuchElementException ex){
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(Map.of(
                        "status", 404,
                        "message", "Resource not found",
                        "detail", ex.getMessage() != null ? ex.getMessage() : "No additional details available"
                ));
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<APIResponse<?>> handleBadCredentials(BadCredentialsException ex) {
        Map<String, String> errors = new HashMap<>();
        errors.put("error", "Invalid username or password");
        
        return new ResponseEntity<>(APIResponse.builder()
                .success(false)
                .message("Authentication failed")
                .errors(errors)
                .data(null)
                .timeStamp(LocalDateTime.now())
                .build(), HttpStatus.UNAUTHORIZED);
    }


    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Object> handleAccessDenied(AccessDeniedException ex){
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(Map.of("status", 403, "message", "Access denied"));
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<APIResponse<?>> handleNotFound(EntityNotFoundException ex) {
        return new ResponseEntity<>(APIResponse.builder()
                .success(false)
                .message(ex.getMessage())
                .errors(null)
                .data(null)
                .timeStamp(LocalDateTime.now())
                .build(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<APIResponse<?>> handleConflict(ConflictException ex) {
        return new ResponseEntity<>(APIResponse.builder()
                .success(false)
                .message(ex.getMessage())
                .errors(null)
                .data(null)
                .timeStamp(LocalDateTime.now())
                .build(), HttpStatus.CONFLICT);
    }
}