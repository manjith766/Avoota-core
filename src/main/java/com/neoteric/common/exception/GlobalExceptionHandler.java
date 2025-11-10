package com.neoteric.common.exception;

import com.neoteric.common.ui.ApiResponse;
import com.neoteric.common.ui.AvootaResponseStatus;
import com.neoteric.common.ui.AvootaUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

    @RestControllerAdvice
    public class GlobalExceptionHandler {

        // Handles your custom business exceptions
        @ExceptionHandler(CustomException.class)
        public ResponseEntity<ApiResponse<?>> handleCustomException(CustomException ex) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(AvootaUtil.failure(ex.getFailureCode()));
        }

        // Handles validation errors (like missing @NotNull fields)
        @ExceptionHandler(MethodArgumentNotValidException.class)
        public ResponseEntity<ApiResponse<?>> handleValidationError(MethodArgumentNotValidException ex) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(AvootaUtil.failure(AvootaResponseStatus.FailureCode.INVALID_INPUT,
                            "Invalid input: " + ex.getFieldError().getDefaultMessage()));
        }

        // Handles any unknown errors (e.g., NullPointer, DB issues)
        @ExceptionHandler(Exception.class)
        public ResponseEntity<ApiResponse<?>> handleGenericException(Exception ex) {
            // Log the error if needed
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(AvootaUtil.genericError());
        }
    }

