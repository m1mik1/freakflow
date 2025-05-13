package com.freakflow.backend.api.advice;

import com.freakflow.backend.domain.exception.InvalidAssociationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // 1) Ошибки @Valid на DTO → 400 + разбивка по полям
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationErrors(
            MethodArgumentNotValidException ex
    ) {
        Map<String, String> errors = new HashMap<>();
        List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();
        for (FieldError fe : fieldErrors) {
            errors.put(fe.getField(), fe.getDefaultMessage());
        }
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(errors);
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<Map<String,String>> handleResponseStatus(ResponseStatusException ex) {
        // ex.getStatus() — то, что ты передал (409), ex.getReason() — твой текст
        return ResponseEntity
                .status(ex.getStatusCode())
                .body(Map.of("error", ex.getReason()));
    }

    // 2) Броски IllegalArgumentException → тоже 400 с единственным полем message
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String,String>> handleIllegalArg(IllegalArgumentException ex) {
        return ResponseEntity
                .badRequest()
                .body(Map.of("error", ex.getMessage()));
    }


    // 3) (Опционально) ловить всё прочее → 500 Internal Server Error
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleAll(Exception ex) {
        // можешь залогировать ex.getMessage()
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "Внутрішня помилка сервера"));
    }

    @ExceptionHandler(InvalidAssociationException.class)
    public ResponseEntity<Map<String, String>> handleInvalid(InvalidAssociationException ex) {
        return ResponseEntity
                .status(HttpStatus.UNPROCESSABLE_ENTITY)       // или 409 /     400
                .body(Map.of("error", ex.getMessage()));
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<Map<String,String>> handleIllegalState(IllegalStateException ex) {
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(Map.of("error", ex.getMessage()));
    }

}
