package org.example.authspringbootsecurity.handler;

import lombok.extern.slf4j.Slf4j;
import org.example.authspringbootsecurity.dto.response.ErrorResponse;
import org.example.authspringbootsecurity.exception.DuplicateResourceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<ErrorResponse> DuplicateResourceExceptionHandler(DuplicateResourceException e, WebRequest request) {
        log.warn("Duplicate resource exception", e);
        String path = request.getDescription(false).replace("uri=", "");
        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .message(e.getMessage())
                .path(path)
                .status(HttpStatus.CONFLICT.value())
                .build();
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
    }
}
